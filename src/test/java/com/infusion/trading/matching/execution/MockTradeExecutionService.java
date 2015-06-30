package com.infusion.trading.matching.execution;

import java.util.ArrayList;
import java.util.List;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.Order;

public class MockTradeExecutionService extends TradeExecutionService {
	private List<Transaction> transactions = new ArrayList<Transaction>();

	@Override
	void sendToClearingEngine(Order order, LimitOrder match, double tradePrice) {
		transactions.add(new Transaction(order, match, tradePrice));
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void reset() {
		transactions.clear();
	}
}
