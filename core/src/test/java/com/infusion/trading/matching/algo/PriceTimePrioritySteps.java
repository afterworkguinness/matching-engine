package com.infusion.trading.matching.algo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.test.common.TestHelper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PriceTimePrioritySteps {

	@Autowired
	private TestHelper testHelper;
	
	@Given("^The order book looks like this before the trade is placed:$")
	public void setupOrderBook(List<LimitOrder> limitOrders) {
		testHelper.setupOrderBook(limitOrders);
	}

	@When("^A (.+) limit order is placed for (\\d+) shares of (.+) at (\\d+)$")
	public void fillLimitOrder(OrderSide side, int quantity, String symbol, double limitPrice) {
		testHelper.fillNewLimitOrder(side, quantity, symbol, limitPrice, null);
	}

	@Then("^The order book should look like this at the end of the trade:$")
	public void verifyOrderBookState(List<LimitOrder> orders) {
		testHelper.verifyOrderBookState(orders);
	}
}