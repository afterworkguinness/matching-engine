package com.infusion.trading.matching.execution;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.Order;

public class MockTradeExecutionService implements ITradeExecutionService {

	private double tradePrice;

	public void executeTrade(Order order, LimitOrder match, double tradePrice) {
		this.tradePrice = tradePrice;
	}

	public double getTradePrice() {
		return tradePrice;
	}
}
