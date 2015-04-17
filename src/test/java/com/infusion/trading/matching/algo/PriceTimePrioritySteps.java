package com.infusion.trading.matching.algo;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.matcher.OrderFillService;
import com.infusion.trading.matching.orderbook.OrderBook;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PriceTimePrioritySteps {

	@Autowired
	private OrderBook orderBook;

	@Autowired
	private OrderFillService orderFillService;

	@Given("^The order book looks like this before the trade is executed:$")
	public void setupOrderBook(List<LimitOrder> limitOrders) {

		orderBook.clear();

		for (LimitOrder order : limitOrders) {
			orderBook.addLimitOrder(order);
		}
	}

	@When("^A (.+) limit order is placed for (\\d+) shares at (\\d+)$")
	public void fillLimitOrder(OrderSide side, int quantity, double limitPrice) {
		orderFillService.processIncomingLimitOrder(new LimitOrder(quantity, limitPrice, side));
	}

	@Then("^The (.+) side of the order book should look like this:$")
	public void verifyOrderBookState(OrderSide side, List<LimitOrder> orders) {

		if (side == OrderSide.BUY) {
			assertEquals(orders, orderBook.getBuyOrders());
		}
		else {
			assertEquals(orders, orderBook.getSellOrders());
		}
	}

}