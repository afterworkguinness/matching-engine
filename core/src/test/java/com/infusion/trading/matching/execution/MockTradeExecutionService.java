package com.infusion.trading.matching.execution;

import java.util.ArrayList;
import java.util.List;

public class MockTradeExecutionService extends TradeExecutionService {
	private List<Transaction> transactions = new ArrayList<Transaction>();

	@Override
	void sendToClearingEngine(Transaction transaction) {
		transactions.add(transaction);
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void reset() {
		transactions.clear();
	}
}
