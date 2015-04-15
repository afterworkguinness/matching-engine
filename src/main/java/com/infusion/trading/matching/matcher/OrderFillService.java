package com.infusion.trading.matching.matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.MarketOrder;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.orderbook.IOrderArrivalTimeService;
import com.infusion.trading.matching.orderbook.OrderBook;

@Component
public class OrderFillService {

	@Autowired
	private OrderBook orderBook;

	@Autowired
	private IOrderArrivalTimeService arrivalTimeService;

	public void processIncomingLimitOrder(LimitOrder incomingLimitOrder) {
		synchronized (this) {

			LimitOrder match = retrieveMatchingOrder(incomingLimitOrder);

			if (match != null) {

				fill(incomingLimitOrder, match);
		
				if(incomingLimitOrder.isCompleted() == false) {
					processIncomingLimitOrder(incomingLimitOrder);
				}
			} else {
				incomingLimitOrder.setArrivalTimeInOrderBook(arrivalTimeService.getArrivalTimeInOrderBook());
				orderBook.addLimitOrder(incomingLimitOrder);
			}
		}
	}

	private void fill(LimitOrder incomingOrder, LimitOrder match) {

		int transactionQuantity = Math.min(incomingOrder.getQuantity(), match.getQuantity());
		incomingOrder.reduceRemainingQuantity(transactionQuantity);
		match.reduceRemainingQuantity(transactionQuantity);
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

					int transactionQuantity = Math.min(incomingOrder.getQuantity(), restingLimitOrder.getQuantity());

					incomingOrder.fill(restingLimitOrder);

					incomingOrder.reduceRemainingQuantity(transactionQuantity);
					restingLimitOrder.reduceRemainingQuantity(transactionQuantity);

					if (restingLimitOrder.isCompleted()) {
						orderBook.removeCompletedOrder(restingLimitOrder.getSide());
					}
				}
			}

			if (incomingOrder.isCompleted() == false) {
				LimitOrder limitOrder = new LimitOrder(incomingOrder.getQuantity(), incomingOrder.getLastTradedPrice(), incomingOrder.getSide());
				limitOrder.setArrivalTimeInOrderBook(arrivalTimeService.getArrivalTimeInOrderBook());
				orderBook.addLimitOrder(limitOrder);
			}
			incomingOrder = null; // I think this helps with expedited GC...
									// double check.
		}
	}

	private LimitOrder retrieveMatchingOrder(LimitOrder incomingOrder) {

		LimitOrder restingLimitOrderAtTopOfBook = orderBook.retrieveOrder(incomingOrder.getSide().getOppositeSide());

		if (OrderSide.BUY == incomingOrder.getSide()) {

			if (incomingOrder.getLimitPrice() <= restingLimitOrderAtTopOfBook.getLimitPrice()) {
				return restingLimitOrderAtTopOfBook;
			}
		} else if (OrderSide.SELL == incomingOrder.getSide()) {

			if (incomingOrder.getLimitPrice() >= restingLimitOrderAtTopOfBook.getLimitPrice()) {
				return restingLimitOrderAtTopOfBook;
			}
		}
		return null;
	}
}
