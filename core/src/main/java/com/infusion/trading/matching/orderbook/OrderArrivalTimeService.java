package com.infusion.trading.matching.orderbook;

import org.springframework.stereotype.Component;

public class OrderArrivalTimeService implements IOrderArrivalTimeService {

	public long getArrivalTimeInOrderBook() {
		return System.currentTimeMillis();
	}
}

