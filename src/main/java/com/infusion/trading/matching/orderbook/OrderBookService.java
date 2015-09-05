package com.infusion.trading.matching.orderbook;

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

		/*
		 * FOR NOW, THIS CODE IS UNREACHABLE SO IT DOESN'T BREAK TESTS MORE
		 * REFACTORING NEEDS TO BE DONE BEFORE THIS CAN BE USED
		 */
		OrderBook book;

		if (orderBooks.containsKey(symbol)) {
			book = orderBooks.get(symbol);
		}
		else {
			book = new OrderBook(arrivalTimeService, orderPlacementAlgorithm);
			orderBooks.put(symbol, book);
			LOGGER.debug("Order book for [" + symbol + "] doesn't exist. Creating.");
		}
		return book;
	}

}
