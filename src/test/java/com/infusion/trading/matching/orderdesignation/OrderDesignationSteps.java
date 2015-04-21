package com.infusion.trading.matching.orderdesignation;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.MarketOrder;
import com.infusion.trading.matching.domain.OrderDesignation;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.matcher.OrderFillService;
import com.infusion.trading.matching.orderbook.OrderBook;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OrderDesignationSteps {

	@Autowired
	private OrderBook orderBook;

	@Autowired
	private OrderFillService orderFillService;

	@Given("^the order book looks like this before the trade is placed$")
	public void setupOrderBook(List<LimitOrder> limitOrders) {

		orderBook.clear();

		for (LimitOrder order : limitOrders) {
			orderBook.addLimitOrder(order);
		}
	}

	@When("^a (.+) market order is placed to (.+) (\\d+) shares$")
	public void processIncomingOrder(OrderDesignation designation, OrderSide side, int quantity) {

		orderFillService.attemptToFillOrder(new MarketOrder(side, quantity, designation));
	}

	@Then("^.*the (.+) side of the order book should look like this:$")
	public void verifyOrderBookState(OrderSide side, List<LimitOrder> orders) {

		if (OrderSide.BUY == side) {
			assertEquals(orders, orderBook.getBuyOrders());
		}
		else {
			assertEquals(orders, orderBook.getSellOrders());
		}
	}
}
