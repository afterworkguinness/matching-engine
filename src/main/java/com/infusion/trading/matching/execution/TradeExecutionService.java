package com.infusion.trading.matching.execution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.Order;

/*
 * Need logic to send details of completed trade (matched buy and sell orders and price) 
 * to some endpoint outside the matching engine so it will clear the trade
 */
@Component
public class TradeExecutionService implements ITradeExecutionService {

	private static final int THREADS = 20; // TODO: Put this in a config file
	private Logger LOGGER = LoggerFactory.getLogger(com.infusion.trading.matching.execution.TradeExecutionService.class);
	private List<Transaction> stagedTransactions = new ArrayList<Transaction>();
	private ExecutorService asyncService = Executors.newFixedThreadPool(THREADS);

	@Override
	public void executeTrade(Order order, LimitOrder match, double tradePrice, boolean holdInStaging) {

		if (holdInStaging) {
			stagedTransactions.add(new Transaction(order, match, tradePrice));
		}
		else {
			new Thread(() -> sendToClearingEngine(order, match, tradePrice)).run();
		}
	}

	@Override
	public void executeStagedTransactions() {
		for (Transaction transaction : stagedTransactions) {
			asyncService.submit(() -> {
				sendToClearingEngine(transaction.getOrder(), transaction.getMatch(), transaction.getTradePrice());
			});
		}
		flushStagedTransactions();
	}

	@Override
	public void flushStagedTransactions() {
		stagedTransactions.clear();
	}

	void sendToClearingEngine(Order order, LimitOrder match, double tradePrice) {
		LOGGER.debug("Trade executed. Sending to clearing engine. Trade price $" + tradePrice);
		// TODO: Implement clearing engine
	}

	protected List<Transaction> getStagedTransactions() {
		return Collections.unmodifiableList(stagedTransactions);
	}
}
