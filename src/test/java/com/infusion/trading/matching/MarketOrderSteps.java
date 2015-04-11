package com.infusion.trading.matching;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.MarketOrder;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.matcher.MatchingEngine;
import com.infusion.trading.matching.orderbook.OrderBook;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MarketOrderSteps {

	@Autowired
	private OrderBook orderbook;
	
	@Autowired
	private MatchingEngine matchingEngine;
	
	@Given("^.+ limit (.+) .+ for (\\d+) shares at (\\d+)$")
	public void addLimitOrderToOrderBook(String orderType, int quantity, int limitPrice) {
		
		LimitOrder limitOrder = new LimitOrder(quantity, limitPrice, OrderSide.SELL);
		
		orderbook.addLimitOrder(limitOrder);
	}
	
	@Given("^these limit orders in the order book$")
	public void addLimitOrdersToOrderBook(List<LimitOrder> orders) {
		orderbook.clear();
		for(LimitOrder order : orders) {
			orderbook.addLimitOrder(order);
		}
		
	}
	
	@When(".+ market (.+) order .+ for (.+) shares")
	public void incomingMarketOrder(String orderType, int quantity) 
	{
		matchingEngine.fillIncomingOrder(new MarketOrder(OrderSide.BUY, quantity));
	}
	
	@Then(".+ be (\\d+) shares left.+")
	public void verifyOrderBookState(int qunatityRemaining) {
		assertTrue(orderbook.getBuyOrders().isEmpty());
		LimitOrder limitOrder = orderbook.getSellOrders().get(0);
		assertEquals(qunatityRemaining,limitOrder.getQuantity());
	}
}
