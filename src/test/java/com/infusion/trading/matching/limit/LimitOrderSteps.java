package com.infusion.trading.matching.limit;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.execution.MockTradeExecutionService;
import com.infusion.trading.matching.test.common.TestHelper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LimitOrderSteps {

	@Autowired
	private TestHelper testHelper;
	
	@Autowired
	private MockTradeExecutionService tradeExecutionService;

	private Logger LOGGER = LoggerFactory.getLogger(LimitOrderSteps.class);

	@Given("^The order book looks like this before the trade is placed:$")
	public void setupOrderbook(List<LimitOrder> limitOrders) {

		testHelper.setupOrderBook(limitOrders);
	}

	@When("^A limit (.+) order is placed for (\\d+) shares of (.+) at (\\d+)$")
	public void addLimitOrder(OrderSide side, int quantity, String symbol, double limitPrice) {
		testHelper.addLimitOrder(side, quantity, symbol, limitPrice, null);
	}

	@Then("^The order book should look like this at the end of the trade:$")
	public void verifyOrderBookState(List<LimitOrder> expectedLmitOrders) {

		testHelper.verifyOrderBookState(expectedLmitOrders);
	}

	@Then("^It crosses the bid ask spread and is executed at (\\d+)$")
	public void verifyCrossesTheSpread(double expectedExecutionPrice) {

		/*
		 * Can't use Mockito to verify TradeExecutionService. To use Mockito
		 * verify, you must execute the code and do the verify right there With
		 * Cucumber, we are splitting up the execution and verification into two
		 * methods Must use a hand rolled stub
		 */
		double actualTradePrice = tradeExecutionService.getTransactions().get(0).getTradePrice();

		assertEquals(expectedExecutionPrice, actualTradePrice, 0.00001);
	}
}
