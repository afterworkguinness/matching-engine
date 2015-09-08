package com.infusion.trading.matching.market;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.test.common.TestHelper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MarketOrderSteps {

	@Autowired
	private TestHelper testHelper;
	
	@Given("^The order book looks like this before the trade is placed:$")
	public void setupOrderBook(List<LimitOrder> orders) {
		testHelper.setupOrderBook(orders);
	}

	@When(".+ market (.+) order .+ for (.+) shares of (.+)")
	public void fillNewMarketOrder(OrderSide side, int quantity, String symbol) {
		testHelper.fillNewMarketOrder(side, quantity, symbol, null);
	}

	@Then("^The order book should look like this at the end of the trade:$")
	public void verifyOrderBookState(List<LimitOrder> expectedLmitOrders) {
		testHelper.verifyOrderBookState(expectedLmitOrders);
	}
}
