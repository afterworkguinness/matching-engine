
package com.infusion.trading.matching.test.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.Order;
import com.infusion.trading.matching.domain.OrderDesignation;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.matcher.OrderFillService;
import com.infusion.trading.matching.orderbook.OrderBook;

@Component
public class BaseSteps {

	@Autowired
	private OrderBook orderBook;
	
	@Autowired
	private OrderFillService orderFillService;
	
	private Order order;
	
	public void setupOrderBook(List<LimitOrder> orders) {
		orderBook.clear();
		
		for(LimitOrder order : orders) {
			orderBook.addLimitOrder(order);
		}
	}
	
	public void addLimitOrder(OrderDesignation designation, OrderSide side, int quantity, double price) {
		this.order=new LimitOrder(quantity, price, side, designation);
		orderFillService.attemptToFillOrder(order);
	}
}
