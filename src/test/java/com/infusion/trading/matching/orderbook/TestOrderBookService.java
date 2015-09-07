package com.infusion.trading.matching.orderbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.algo.IOrderPlacementAlgorithm;

@Component
public class TestOrderBookService extends OrderBookService {

	@Autowired
	private IOrderArrivalTimeService arrivalTimeService;

	@Autowired
	private IOrderPlacementAlgorithm orderPlacementAlgorithm;

	public OrderBook forceCreateNewOrderBook(String symbol) {

		return new OrderBook(arrivalTimeService, orderPlacementAlgorithm, symbol);
	}
}
