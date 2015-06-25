package com.infusion.trading.matching.execution;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.Order;

public interface ITradeExecutionService {

	public void executeTrade(Order order, LimitOrder match, double tradePrice, boolean holdInStaging);

	public void executeStagedTransactions();

	public void flushStagedTransactions();

}
