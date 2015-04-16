package com.infusion.trading.matching.limit;

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

public class LimitOrderSteps {

	@Autowired
	private OrderBook orderBook;

	@Autowired
	private OrderFillService orderFillService;

	@Given("^The order book looks like this before the trade is placed:$")
	public void setupOrderbook(List<LimitOrder> limitOrders) {

		orderBook.clear();

		for (LimitOrder order : limitOrders)
			orderBook.addLimitOrder(order);
	}

	@When("^A limit (.+) order is placed for (\\d+) shares at (\\d+)$")
	public void addLimitOrder(OrderSide side, int quantity, double limitPrice) {
		orderFillService.processIncomingLimitOrder(new LimitOrder(quantity, limitPrice, side));
	}

	@Then("^The (.+) side of the order book should look like this at the end of the trade:$")
	public void verifyOrderBookState(OrderSide side, List<LimitOrder> limitOrders) {

		if (OrderSide.BUY == side) {
			assertEquals(limitOrders, orderBook.getBuyOrders());
		}
		else {
			assertEquals(limitOrders, orderBook.getSellOrders());
		}
	}
}
