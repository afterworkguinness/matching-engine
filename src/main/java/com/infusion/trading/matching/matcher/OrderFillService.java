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

	public void processIncomingLimitOrder(LimitOrder incomingLimitOrder) {

		synchronized (this) {

			LimitOrder match = retrieveMatchingOrder(incomingLimitOrder);

			if (match != null) {

				fill(incomingLimitOrder, match);
				/*
				 * Transaction price: If the incoming order is a market order,
				 * it has no price limits and the price of the matched limit
				 * order is used If the incoming order is a limit order, the
				 * price of the older order is used ...that is always the
				 * resting order
				 * 
				 * So in any case, the price of the resting order is used
				 */
				tradeExecutionService.executeTrade(incomingLimitOrder, match, match.getLimitPrice());

				if (incomingLimitOrder.isCompleted() == false) {
					processIncomingLimitOrder(incomingLimitOrder);
				}
			}
			else {
				orderBook.addLimitOrder(incomingLimitOrder);
			}
		}
	}

	private void fill(Order incomingOrder, LimitOrder match) {

		int transactionQuantity = Math.min(incomingOrder.getQuantity(), match.getQuantity());
		incomingOrder.reduceRemainingQuantity(transactionQuantity);
		match.reduceRemainingQuantity(transactionQuantity);

		if (match.isCompleted()) {
			orderBook.removeCompletedOrder(match.getSide());
		}
	}

	public void fillIncomingMarketOrder(MarketOrder incomingOrder) {

		/*
		 * Order comes in if market order, execute immediately if limit order,
		 * check if executable
		 */

		synchronized (this) {

			while (incomingOrder.isCompleted() == false && orderBook.isLiquidityLeft(incomingOrder.getSide().getOppositeSide())) {

				LimitOrder restingLimitOrder = orderBook.retrieveOrder(incomingOrder.getSide().getOppositeSide());

				if (restingLimitOrder != null) {

					fill(incomingOrder, restingLimitOrder);
					incomingOrder.setLastTradedPrice(restingLimitOrder.getLimitPrice());

					/*
					 * Transaction price: If the incoming order is a market
					 * order, it has no price limits and the price of the
					 * matched limit order is used If the incoming order is a
					 * limit order, the price of the older order is used ...that
					 * is always the resting order
					 * 
					 * So in any case, the price of the resting order is used
					 */
					tradeExecutionService.executeTrade(incomingOrder, restingLimitOrder, restingLimitOrder.getLimitPrice());
				}
			}

			if (incomingOrder.isCompleted() == false) {
				LimitOrder limitOrder = new LimitOrder(incomingOrder.getQuantity(), incomingOrder.getLastTradedPrice(), incomingOrder.getSide());
				orderBook.addLimitOrder(limitOrder);
			}
			incomingOrder = null; // I think this helps with expedited GC...
									// double check.
		}
	}

	private LimitOrder retrieveMatchingOrder(LimitOrder incomingOrder) {

		LimitOrder restingLimitOrderAtTopOfBook = orderBook.retrieveOrder(incomingOrder.getSide().getOppositeSide());

		if (restingLimitOrderAtTopOfBook == null) {
			return null; // no liquidity
		}

		if (OrderSide.BUY == incomingOrder.getSide()) {

			if (incomingOrder.getLimitPrice() >= restingLimitOrderAtTopOfBook.getLimitPrice()) {
				return restingLimitOrderAtTopOfBook;
			}
		}
		else if (OrderSide.SELL == incomingOrder.getSide()) {

			if (incomingOrder.getLimitPrice() <= restingLimitOrderAtTopOfBook.getLimitPrice()) {
				return restingLimitOrderAtTopOfBook;
			}
		}
		return null;
	}
}
