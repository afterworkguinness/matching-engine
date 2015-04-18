package com.infusion.trading.matching.execution;

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

	private Logger LOGGER = LoggerFactory.getLogger(com.infusion.trading.matching.execution.TradeExecutionService.class);

	@Override
	public void executeTrade(Order order, LimitOrder match, double tradePrice) {

		new Thread(() -> sendToClearingEngine(order, match, tradePrice)).run();
	}

	private void sendToClearingEngine(Order order, LimitOrder match, double tradePrice) {
		LOGGER.debug("Trade executed. Sending to clearing engine. Trade price $" + tradePrice);
		// TODO: Implement clearing engine
	}
}
