package com.infusion.trading.matching;

import static org.junit.Assert.assertEquals;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MarketOrderSteps {

	private int totalShares;
	
	@Given("^.+ limit (.+) .+ for (\\d+) shares at (\\d+)$")
	public void addLimitOrderToOrderBook(String orderType, int quantity, int limitPrice) {
		totalShares=quantity;
	}
	
	@When(".+ market (.+) order .+ for (.+) shares")
	public void addMarketOrder(String orderType, int quantity) {
		totalShares-=quantity;
	}
	
	@Then(".+ be (\\d+) shares left.+")
	public void doNothing(int qunatityRemaining) {
		assertEquals(qunatityRemaining, totalShares);
	}
}
