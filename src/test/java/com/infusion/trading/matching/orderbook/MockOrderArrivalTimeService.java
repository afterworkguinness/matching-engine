package com.infusion.trading.matching.orderbook;

import com.infusion.trading.matching.orderbook.IOrderArrivalTimeService;

public class MockOrderArrivalTimeService implements IOrderArrivalTimeService {

	public long getArrivalTimeInOrderBook() {
		return 0;
	}

	public void recordArrivalTimeInOrderBook() {
	}
}
