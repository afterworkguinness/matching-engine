package com.infusion.trading.matching.orderbook;

import org.springframework.stereotype.Component;

public class MockOrderArrivalTimeService implements IOrderArrivalTimeService {

	public long getArrivalTimeInOrderBook() {
		return 0;
	}

	public void recordArrivalTimeInOrderBook() {
	}
}
