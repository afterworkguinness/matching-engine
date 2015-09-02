package com.infusion.trading.matching.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.Order;
import com.infusion.trading.matching.domain.OrderDesignation;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.matcher.OrderFillService;
import com.infusion.trading.matching.orderbook.OrderBook;
import com.infusion.trading.matching.test.common.BaseSteps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ExecuteOrderSteps {

	@Autowired
	private OrderBook orderBook;
	
	@Autowired
	private MockTradeExecutionService tradeExecutionService;
	
	@Autowired
	private OrderFillService orderFillService;
	
	@Autowired
	private BaseSteps baseSteps;
	
	private Order restingOrder_buy;
	private Order expectedMatch_sell;
	
	
	@Given("^The order book looks like this before the trade is placed:$")
	public void setupOrderBook(List<LimitOrder> limitOrders) {
		
		//In this test, we're only expecting a single order to be waiting in the book
		restingOrder_buy = limitOrders.get(0);
		
		tradeExecutionService.reset();
		baseSteps.setupOrderBook(limitOrders);
	}
	
	@When("^A limit (.+) order is placed for (\\d+) shares at (\\d+)$")
	public void addLimitOrder(OrderSide side, int quantity, double price) {
		baseSteps.addLimitOrder(null, side, quantity, price);
	}
	
	@When("^A (.+) limit (.+) order is placed for (\\d+) shares at (\\d+)$")
	public void addLimitOrder(OrderDesignation designation, OrderSide side, int quantity, double price) {
		baseSteps.addLimitOrder(designation, side, quantity, price);
	}

	@Then("^No trades should be executed")
	public void verifyNoTradesExecuted() {
		assertTrue(tradeExecutionService.getTransactions().isEmpty());
	}
	
	@Then("^A trade for (\\d+) shares should be executed at (\\d+)")
	public void verifyTransactionExecuted(int quantity, double price) {
		List<Transaction> transactionsSentToClearingHouse = tradeExecutionService.getTransactions();
		
		assertFalse("transactions should not be empty", transactionsSentToClearingHouse.isEmpty());
		
		/* complete flag on limit order is not publicly mutable and is 
		 * used in testing equality on match and order fields of transaction.
		 * 
		 * For that reason, can't simply compare expected and actual  transactions, 
		 * need to compare property by property
		 */
		
		Transaction actualTransaction = transactionsSentToClearingHouse.get(0);
		assertEquals(price, actualTransaction.getTradePrice(), 0.0001);
		assertEquals(quantity, actualTransaction.getQuantity(), 0.0001);
	}
}
