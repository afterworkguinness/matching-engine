package com.infusion.trading.matching.orderbook;

import com.infusion.trading.matching.orderbook.IOrderArrivalTimeService;

public class MockOrderArrivalTimeService implements IOrderArrivalTimeService {

	@Override
	public long getArrivalTimeInOrderBook() {
		return 0;
	}

	@Override
	public void recordArrivalTimeInOrderBook() {
	}

}
