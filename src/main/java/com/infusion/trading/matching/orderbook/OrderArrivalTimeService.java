package com.infusion.trading.matching.orderbook;

public class OrderArrivalTimeService implements IOrderArrivalTimeService {

	public long getArrivalTimeInOrderBook() {
		return System.currentTimeMillis();
	}
}
