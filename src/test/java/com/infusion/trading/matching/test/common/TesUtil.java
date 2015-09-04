package com.infusion.trading.matching.test.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.domain.Order;
import com.infusion.trading.matching.orderbook.OrderBook;
import com.infusion.trading.matching.orderbook.OrderBookService;

@Component
public class TesUtil {

	@Autowired
	private OrderBookService orderBookService;

	public OrderBook getOrderBook(Order order) {
		String symbol = null;

		if (order != null) {
			symbol = order.getSymbol();
		}

		return orderBookService.getOrderBook(symbol);
	}
}
