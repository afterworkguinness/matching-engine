
package com.infusion.trading.matching.test.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.orderbook.OrderBook;

@Component
public class BaseSteps {

	@Autowired
	private OrderBook orderBook;
	
	public void setupOrderBook(List<LimitOrder> orders) {
		orderBook.clear();
		
		for(LimitOrder order : orders) {
			orderBook.addLimitOrder(order);
		}
	}
}
