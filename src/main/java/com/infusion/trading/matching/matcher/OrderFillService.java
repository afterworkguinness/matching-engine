package com.infusion.trading.matching.matcher;

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

			fillOrderUntilNoMatchesOrNoLiquidiy(order);
			
			if (order.isCompleted() == false) {
				addOrderToBook(order);
			}
		}
	}

	private void fillOrderUntilNoMatchesOrNoLiquidiy(Order order) {
	
		while (order.isCompleted() == false && orderBook.isLiquidityLeft(order.getSide().getOppositeSide())) {

			LimitOrder match = findMatchingOrder(order);

			if (match != null) {
				fill(order, match);
				tradeExecutionService.executeTrade(order, match, match.getLimitPrice());
			} else {
				break;
			}
		}
	}

	private void addOrderToBook(Order order) {
		LimitOrder limitOrder;

		if (order instanceof MarketOrder) {
			limitOrder = new LimitOrder(order.getQuantity(), order.getLastTradedPrice(), order.getSide());
		} else {
			limitOrder = (LimitOrder) order;
		}
		orderBook.addLimitOrder(limitOrder);
	}

	private void fill(Order incomingOrder, LimitOrder match) {

		int transactionQuantity = Math.min(incomingOrder.getQuantity(), match.getQuantity());
		incomingOrder.reduceRemainingQuantity(transactionQuantity);
		match.reduceRemainingQuantity(transactionQuantity);
		// Last traded price is not needed for a limit order, but for simplicity
		// we will set it regardless
		incomingOrder.setLastTradedPrice(match.getLimitPrice());

		if (match.isCompleted()) {
			orderBook.removeCompletedOrder(match.getSide());
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
		} else if (OrderSide.SELL == order.getSide()) {

			if (limitOrder.getLimitPrice() <= orderAtTopOfBook.getLimitPrice()) {
				return orderAtTopOfBook;
			}
		}
		return null;
	}
}
