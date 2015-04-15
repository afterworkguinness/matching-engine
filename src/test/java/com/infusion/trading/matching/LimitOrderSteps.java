package com.infusion.trading.matching;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.matcher.OrderFillService;
import com.infusion.trading.matching.orderbook.OrderBook;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class LimitOrderSteps {

	@Autowired
	private OrderBook orderBook;

	@Autowired
	private OrderFillService matcher;

	@Given("^The order book looks like this before the trade is placed:$")
	public void setupOrderbook(List<LimitOrder> limitOrders) {

		orderBook.clear();

		for (LimitOrder order : limitOrders)
			orderBook.addLimitOrder(order);
	}

	@When("^A limit (.+) order is palced for (\\d+) shares at (\\d+)$")
	public void addLimitOrder(OrderSide side, int quantity, double limitPrice) {
		orderBook.addLimitOrder(new LimitOrder(quantity, limitPrice, side));
	}
}
