package com.infusion.trading.matching.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.Order;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.matcher.OrderFillService;
import com.infusion.trading.matching.orderbook.OrderBook;

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
	
	private Order order;
	
	@Given("^The order book looks like this before the trade is placed:$")
	public void setupOrderBook(List<LimitOrder> limitOrders) {
		
		orderBook.clear();
		tradeExecutionService.reset();
		
		for(LimitOrder order : limitOrders) {
			orderBook.addLimitOrder(order);
		}
	}
	
	@When("^A limit (.+) order is placed for (\\d+) shares at (\\d+)$")
	public void addLimitOrder(OrderSide side, int quantity, double price) {
		this.order=new LimitOrder(quantity, price, side);
		orderFillService.attemptToFillOrder(order);
	}

	@Then("^A trade for (\\d+) shares should be executed at (\\d+)")
	public void verifyTransactionExecuted(int quantity, double price) {
		List<Transaction> transactionsSentToClearingHouse = tradeExecutionService.getTransactions();
		
		assertFalse("transactions should not be empty", transactionsSentToClearingHouse.isEmpty());
		//It would be nice to be able to set completed flag
		//Setting quantity to 0 as it will be 0 after filling order
		LimitOrder expectedMatch = new LimitOrder(0, 150, OrderSide.BUY);
		Transaction expectedTransaction = new Transaction(order, expectedMatch, 150);
		
		/* complete flag on limit order is not publicly mutable and is 
		 * used in testing equality on match and order fields of transaction.
		 * 
		 * For that reason, can't simply compare expected and actual  transactions, 
		 * need to compare property by property
		 */
		
		Transaction actualTransaction = transactionsSentToClearingHouse.get(0);
		LimitOrder actualMatch = actualTransaction.getMatch();
				
		assertEquals(expectedTransaction.getOrder(), actualTransaction.getOrder());
		assertEquals(expectedMatch.getQuantity(), actualMatch.getQuantity());
		assertEquals(expectedMatch.getSide(), actualMatch.getSide());
		assertEquals(price, actualTransaction.getTradePrice(), 0.0001);
	}
}
