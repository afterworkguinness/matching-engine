package com.infusion.trading.matching.orderdesignation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.OrderDesignation;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.test.common.TestHelper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OrderDesignationSteps {

	@Autowired
	private TestHelper testHelper;

	@Given("^the order book looks like this before the trade is placed:$")
	public void setupOrderBook(List<LimitOrder> limitOrders) {
		testHelper.setupOrderBook(limitOrders);
	}

	@When("^a (.+) market (.+) order is placed for (\\d+) shares of (.+)$")
	public void processIncomingOrder(OrderDesignation designation, OrderSide side, int quantity, String symbol) {
		testHelper.fillNewMarketOrder(side, quantity, symbol, designation);
	}

	@Then("^.*the order book should look like this at the end of the trade:$")
	public void verifyOrderBookState(List<LimitOrder> orders) {
		testHelper.verifyOrderBookState(orders);
	}
}
