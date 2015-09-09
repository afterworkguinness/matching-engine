package com.infusion.trading.matching.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.OrderDesignation;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.test.common.TestHelper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ExecuteOrderSteps {

	@Autowired
	private MockTradeExecutionService tradeExecutionService;

	@Autowired
	private TestHelper testHelper;

	@Given("^The order book looks like this before the trade is placed:$")
	public void setupOrderBook(List<LimitOrder> limitOrders) {
		tradeExecutionService.reset();
		testHelper.setupOrderBook(limitOrders);
	}
	
	@When("^A (.+) limit (.+) order is placed for (\\d+) shares of (.+) at (\\d+)$")
	public void addLimitOrder(OrderDesignation designation, OrderSide side, int quantity, String symbol, double price) {
		testHelper.fillNewLimitOrder(side, quantity, symbol, price, designation);
	}

	@When("^A limit (.+) order is placed for (\\d+) shares of (.+) at (\\d+)$")
	public void addLimitOrder(OrderSide side, int quantity, String symbol, double price) {
		testHelper.fillNewLimitOrder(side, quantity, symbol, price, null);
	}

	@Then("^No trades should be executed")
	public void verifyNoTradesExecuted() {
		assertTrue(tradeExecutionService.getTransactions().isEmpty());
	}

	@Then("^A trade for (\\d+) shares of (.+) should be executed at (\\d+)")
	public void verifyTransactionsExecuted(int quantity, String symbol, double price) {

		List<Transaction> transactionsSentToClearingHouse = tradeExecutionService.getTransactions();

		assertFalse("transactions should not be empty", transactionsSentToClearingHouse.isEmpty());

		// TODO
		/*
		 * complete flag on limit order is not publicly mutable and is used in
		 * testing equality on match and order fields of transaction.
		 * 
		 * For that reason, can't simply compare expected and actual
		 * transactions, need to compare property by property
		 */

		Transaction actualTransaction = transactionsSentToClearingHouse.get(0);
		assertEquals(price, actualTransaction.getTradePrice(), 0.0001);
		assertEquals(quantity, actualTransaction.getQuantity(), 0.0001);
		assertEquals(symbol, actualTransaction.getSymbol());
	}

	@Then("^the following trades are executed:$")
	public void verifyTransactionsExecuted(List<Transaction> expectedTransactions) {

		List<Transaction> actualTransactions = tradeExecutionService.getTransactions();
		assertEquals(expectedTransactions, actualTransactions);
	}
}
