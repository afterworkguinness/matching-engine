package com.infusion.trading.matching;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.matcher.MatchingEngine;
import com.infusion.trading.matching.orderbook.OrderBook;

public class LimitOrderSteps {

	@Autowired
	private OrderBook orderBook;

	@Autowired
	private MatchingEngine matcher;

}
