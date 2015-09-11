package com.infusion.trading.matching.orderbook;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;

import com.infusion.trading.matching.algo.IOrderPlacementAlgorithm;

@Component
public class OrderBookService {

	@Autowired
	private IOrderArrivalTimeService arrivalTimeService;

	@Autowired
	private IOrderPlacementAlgorithm orderPlacementAlgorithm;

	// We want to directly modify the map from a test so make it accessible to
	// classes in its package
	Map<String, OrderBook> orderBooks = new HashMap<String, OrderBook>();
	private Logger LOGGER = LoggerFactory.getLogger(OrderBookService.class);

	public OrderBook getOrderBook(String symbol) {

		OrderBook book;

		if (orderBooks.containsKey(symbol)) {
			book = orderBooks.get(symbol);
		}
		else {
			book = new OrderBook(arrivalTimeService, orderPlacementAlgorithm, symbol);
			orderBooks.put(symbol, book);
			LOGGER.debug("Order book for [" + symbol + "] doesn't exist. Creating.");
		}
		return book;
	}

	/*
	 * Somehow, some way, instead of returning orderbooks just return immutable
	 * lists of buy/sell orders
	 */
	public Map<String, OrderBook> getAllOrderBooks() {
		return Collections.unmodifiableMap(orderBooks);
	}

}
