package com.infusion.trading.matching.execution;


public interface ITradeExecutionService {

	public void executeTrade(double tradePrice, int quantity, boolean holdInStaging, long buyTradeId, long sellTradeId);

	public void executeStagedTransactions();

	public void flushStagedTransactions();

}
