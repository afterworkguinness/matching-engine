package com.infusion.trading.matching.matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.MarketOrder;
import com.infusion.trading.matching.domain.Order;
import com.infusion.trading.matching.execution.ITradeExecutionService;
import com.infusion.trading.matching.orderbook.OrderBook;

@Component
public class OrderFillService {

	@Autowired
	private OrderBook orderBook;

	@Autowired
	private ITradeExecutionService tradeExecutionService;

	@Autowired
	private OrderMatchService orderMatchService;

	private Logger LOGGER = LoggerFactory.getLogger(com.infusion.trading.matching.matcher.OrderFillService.class);

	public void attemptToFillOrder(Order order) {

		/*
		 * Orders are processed one by one, never in parallel
		 */

		/*
		 * Transaction price: If the incoming order is a market order, it has no
		 * price limits and the price of the matched limit order is used If the
		 * incoming order is a limit order, the price of the older order is used
		 * ...that is always the resting order
		 * 
		 * So in any case, the price of the resting order is used
		 */
		synchronized (this) {

			LOGGER.debug("New order - " + order);

			fillOrderUntilNoMatchesOrNoLiquidiy(order);

			if (order.isCompleted()) {
				processCompleteOrder(order);
			}
			else {
				processIncompleteOrder(order);
			}
		}
	}

	private void processCompleteOrder(Order order) {
		LOGGER.debug("Order is completed");
		tradeExecutionService.executeStagedTransactions();
		orderBook.completeStagedOrders(order.getSide().getOppositeSide());
	}

	private void processIncompleteOrder(Order order) {
		LOGGER.debug("Order is incomplete and no more matches exist in book.");

		if (order.isPartialFillsAllowed()) {
			LOGGER.debug("Adding to order book");
			addOrderToBook(order);
		}
		else {
			LOGGER.debug("Partial fills not allowed for this order. Killing and reverting any limit orders used to fill it");
			orderBook.revertStagedOrders(order.getSide().getOppositeSide());
			tradeExecutionService.flushStagedTransactions();
		}
	}

	private void fillOrderUntilNoMatchesOrNoLiquidiy(Order order) {

		while (order.isCompleted() == false && orderBook.isLiquidityLeft(order.getSide())) {

			LimitOrder match = orderMatchService.findMatchingOrder(order);

			if (match != null) {
				LOGGER.debug("Matching order found - " + match);

				int transactionQuantity = Math.min(order.getQuantity(), match.getQuantity());

				LOGGER.debug("Transaction quantity is " + transactionQuantity);

				order.reduceRemainingQuantity(transactionQuantity);
				LOGGER.debug("Incoming order remaining quantity is " + order.getQuantity());

				match.reduceRemainingQuantity(transactionQuantity);
				LOGGER.debug("Matched order remaining quantity is " + match.getQuantity());

				// Last traded price is not needed for a limit order, but for
				// simplicity
				// we will set it regardless
				LOGGER.info("Last traded price: " + match.getLimitPrice());
				order.setLastTradedPrice(match.getLimitPrice());

				if (match.isCompleted()) {
					LOGGER.debug("Matched order completely filled");
					orderBook.removeCompletedOrder(match.getSide(), order.isPartialFillsAllowed());
				}
				tradeExecutionService.executeTrade(match.getLimitPrice(), transactionQuantity, !order.isPartialFillsAllowed(), 0,0);
			}
			else {
				LOGGER.debug("No matching order found");
				break;
			}
		}
	}

	private void addOrderToBook(Order order) {
		LimitOrder limitOrder;

		if (order instanceof MarketOrder) {
			LOGGER.debug("Converting market order to limit order at last traded price $" + order.getLastTradedPrice());
			limitOrder = new LimitOrder(order.getQuantity(), order.getLastTradedPrice(), order.getSide());
		}
		else {
			limitOrder = (LimitOrder) order;
		}
		orderBook.addLimitOrder(limitOrder);
	}

}
