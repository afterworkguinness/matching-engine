package com.infusion.trading.matching.orderbook;

public class OrderArrivalTimeService implements IOrderArrivalTimeService {

	private long arrivalTime;

	public long getArrivalTimeInOrderBook() {
		return arrivalTime;
	}

	public void recordArrivalTimeInOrderBook() {
		this.arrivalTime = System.currentTimeMillis();
	}
}
