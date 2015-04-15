package com.infusion.trading.matching.orderbook;

import org.springframework.stereotype.Component;

@Component
public class MockOrderArrivalTimeService implements IOrderArrivalTimeService {

	@Override
	public long getArrivalTimeInOrderBook() {
		return 0;
	}

	@Override
	public void recordArrivalTimeInOrderBook() {
	}
}
