package com.infusion.trading.matching.orderbook;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderBookService {
	
	private Map<String, OrderBook> orderBooks = new HashMap<String, OrderBook>();
	private Logger LOGGER = LoggerFactory.getLogger(OrderBookService.class);
	
	public OrderBook getOrderBook(String symbol) {
		
		OrderBook book;
		
		if(orderBooks.containsKey(symbol)) {
			book = orderBooks.get(symbol);
		} else {
			book = new OrderBook();
			orderBooks.put(symbol, book);
			LOGGER.debug("Order book for ["+symbol+"] doesn't exist. Creating.");
		}
		return book;
	}
}
