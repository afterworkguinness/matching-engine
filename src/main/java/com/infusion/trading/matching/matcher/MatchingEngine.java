package com.infusion.trading.matching.matcher;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.MarketOrder;
import com.infusion.trading.matching.orderbook.OrderBook;

public class MatchingEngine {

	@Autowired
	private OrderBook	orderBook;

	public void fillIncomingOrder(MarketOrder order) {

		/*
		 * Order comes in if market order, execute immediately if limit order,
		 * check if executable
		 */

		synchronized (this) {
			if (order instanceof MarketOrder) {
				MarketOrder marketOrder = (MarketOrder) order;

				LimitOrder limitOrder = orderBook.retrieveOrder(marketOrder.getSide().getOppositeSide());

				if (limitOrder != null) {
					
					int transactionQuantity = Math.min(marketOrder.getQuantity(), limitOrder.getQuantity());
					marketOrder.fill(limitOrder);
					marketOrder.reduceRemainingQuantity(transactionQuantity);
					
					limitOrder.fill(marketOrder);
					limitOrder.reduceRemainingQuantity(transactionQuantity);
				}
			}
		}
	}
}
