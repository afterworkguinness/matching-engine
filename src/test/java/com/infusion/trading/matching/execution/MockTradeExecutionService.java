package com.infusion.trading.matching.execution;

import java.util.ArrayList;
import java.util.List;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.Order;

public class MockTradeExecutionService extends TradeExecutionService {
	private List<Transaction> transactions = new ArrayList<Transaction>();

	@Override
	public void executeTrade(Order order, LimitOrder match, double tradePrice, boolean holdInStaging) {
		transactions.add(new Transaction(order, match, tradePrice));
		super.executeTrade(order, match, tradePrice, holdInStaging);
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void reset() {
		transactions.clear();
	}
}
