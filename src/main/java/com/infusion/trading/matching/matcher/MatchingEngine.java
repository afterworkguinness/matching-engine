package com.infusion.trading.matching.matcher;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.MarketOrder;
import com.infusion.trading.matching.orderbook.OrderBook;

public class MatchingEngine {

	@Autowired
	private OrderBook orderBook;

	public void fillIncomingOrder(MarketOrder order) {

		/*
		 * Order comes in if market order, execute immediately if limit order,
		 * check if executable
		 */

		synchronized (this) {
			if (order instanceof MarketOrder) {

				MarketOrder incomingOrder = order;

				while (incomingOrder.isCompleted() == false && orderBook.isLiquidityLeft(incomingOrder.getSide().getOppositeSide())) {

					LimitOrder restingLimitOrder = orderBook.retrieveOrder(incomingOrder.getSide().getOppositeSide());

					if (restingLimitOrder != null) {

						int transactionQuantity = Math.min(incomingOrder.getQuantity(), restingLimitOrder.getQuantity());

						incomingOrder.fill(restingLimitOrder);

						incomingOrder.reduceRemainingQuantity(transactionQuantity);
						restingLimitOrder.reduceRemainingQuantity(transactionQuantity);

						if (restingLimitOrder.isCompleted()) {
							orderBook.removeCompletedOrder(restingLimitOrder.getSide());
						}
					}
				}
				
				if(incomingOrder.isCompleted() == false) {
					LimitOrder limitOrder = new LimitOrder(incomingOrder.getQuantity(), incomingOrder.getLastTradedPrice(), incomingOrder.getSide());
					orderBook.addLimitOrder(limitOrder);
				}
				incomingOrder=null; //I think this helps with expedited GC... double check.
			}
		}
	}
}
