package com.infusion.trading.matching.orderbook;

public class OrderArrivalTimeService implements IOrderArrivalTimeService {

	private long arrivalTime;
	
	@Override
	public long getArrivalTimeInOrderBook() {
		return arrivalTime;
	}

	@Override
	public void recordArrivalTimeInOrderBook() {
		this.arrivalTime=System.currentTimeMillis();
	}
}
