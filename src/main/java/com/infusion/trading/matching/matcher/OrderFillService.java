package com.infusion.trading.matching.matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.MarketOrder;
import com.infusion.trading.matching.domain.Order;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.execution.ITradeExecutionService;
import com.infusion.trading.matching.orderbook.OrderBook;

@Component
public class OrderFillService {

	@Autowired
	private OrderBook orderBook;

	@Autowired
	private ITradeExecutionService tradeExecutionService;

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
				LOGGER.debug("Order is completed");
			}
			if (order.isCompleted() == false) {
				LOGGER.debug("Order is incomplete and no more matches exist in book. Adding to order book");
				addOrderToBook(order);
			}
		}
	}

	private void fillOrderUntilNoMatchesOrNoLiquidiy(Order order) {

		while (order.isCompleted() == false && orderBook.isLiquidityLeft(order.getSide().getOppositeSide())) {

			LimitOrder match = findMatchingOrder(order);

			if (match != null) {
				LOGGER.debug("Matching order found - " + match);
				fill(order, match);
				tradeExecutionService.executeTrade(order, match, match.getLimitPrice());
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

	private void fill(Order incomingOrder, LimitOrder match) {

		int transactionQuantity = Math.min(incomingOrder.getQuantity(), match.getQuantity());

		LOGGER.debug("Transaction quantity is " + transactionQuantity);

		incomingOrder.reduceRemainingQuantity(transactionQuantity);
		LOGGER.debug("Incoming order remaining quantity is " + incomingOrder.getQuantity());

		match.reduceRemainingQuantity(transactionQuantity);
		LOGGER.debug("Matched order remaining quantity is " + match.getQuantity());

		// Last traded price is not needed for a limit order, but for simplicity
		// we will set it regardless
		incomingOrder.setLastTradedPrice(match.getLimitPrice());

		if (match.isCompleted()) {
			LOGGER.debug("Matched order completely filled");
			orderBook.removeCompletedOrder(match.getSide(), incomingOrder.isPartialFillsAllowed());
		}
	}

	private LimitOrder findMatchingOrder(Order order) {

		LimitOrder orderAtTopOfBook = orderBook.retrieveOrder(order.getSide().getOppositeSide());
		// Liquidity is checked before calling this method, so if it returns
		// null that's b/c a it doesn't match the limit orders price limit
		if (order instanceof MarketOrder || orderAtTopOfBook == null) {
			return orderAtTopOfBook;
		}

		LimitOrder limitOrder = (LimitOrder) order;

		if (OrderSide.BUY == order.getSide()) {

			if (limitOrder.getLimitPrice() >= orderAtTopOfBook.getLimitPrice()) {
				return orderAtTopOfBook;
			}
		}
		else if (OrderSide.SELL == order.getSide()) {

			if (limitOrder.getLimitPrice() <= orderAtTopOfBook.getLimitPrice()) {
				return orderAtTopOfBook;
			}
		}
		return null;
	}
}
