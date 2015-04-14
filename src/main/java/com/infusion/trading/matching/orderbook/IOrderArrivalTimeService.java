package com.infusion.trading.matching.orderbook;

public interface IOrderArrivalTimeService {
	
	long getArrivalTimeInOrderBook();
	void recordArrivalTimeInOrderBook();
}
