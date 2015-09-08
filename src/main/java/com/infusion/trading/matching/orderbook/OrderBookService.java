package com.infusion.trading.matching.orderbook;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.algo.IOrderPlacementAlgorithm;

@Component
public class OrderBookService {

	@Autowired
	private OrderBook tempOrderBook;

	@Autowired
	private IOrderArrivalTimeService arrivalTimeService;

	@Autowired
	private IOrderPlacementAlgorithm orderPlacementAlgorithm;

	private Map<String, OrderBook> orderBooks = new ConcurrentHashMap<String, OrderBook>();
	private Logger LOGGER = LoggerFactory.getLogger(OrderBookService.class);

	public OrderBook getOrderBook(String symbol) {

		if (symbol == null)
			return tempOrderBook;

		OrderBook book;

		if (orderBooks.containsKey(symbol)) {
			book = orderBooks.get(symbol);
		}
		else {
			/* TODO: Very odd behaviour (and replicatable).
			 * I create FOO order book and add an order to it in the setup steps
			 * I then create a Bar orderbook. When its constructor returns, it has a foo order in it... HOW ???
			 */
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
