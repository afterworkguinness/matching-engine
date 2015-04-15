package com.infusion.trading.matching;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.MarketOrder;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.lmit.LimitOrder;
import com.infusion.trading.matching.lmit.LimitOrderDetails;
import com.infusion.trading.matching.lmit.LimitOrderFactory;
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

	@Autowired
	private LimitOrderFactory limitOrderFactory;

	@Given("^.+ limit (.+) .+ for (\\d+) shares at (\\d+)$")
	public void addLimitOrderToOrderBook(String orderType, int quantity, int limitPrice) {
		LimitOrderDetails orderDetails = new LimitOrderDetails(quantity, limitPrice, OrderSide.SELL);
		orderbook.addLimitOrder(limitOrderFactory.createLimitOrder(orderDetails));
	}

	@Given("^these limit orders in the order book$")
	public void addLimitOrdersToOrderBook(List<LimitOrderDetails> orders) {
		orderbook.clear();
		for (LimitOrderDetails order : orders) {
			orderbook.addLimitOrder(limitOrderFactory.createLimitOrder(order));
		}
	}

	@When(".+ market (.+) order .+ for (.+) shares")
	public void incomingMarketOrder(String orderType, int quantity) {
		matchingEngine.fillIncomingOrder(new MarketOrder(OrderSide.BUY, quantity));
	}

	@Then(".+ be (\\d+) shares left.+")
	public void verifyOrderBookState(int qunatityRemaining) {
		assertTrue(orderbook.getBuyOrders().isEmpty());
		LimitOrder limitOrder = orderbook.getSellOrders().get(0);
		assertEquals(qunatityRemaining, limitOrder.getOrderDetails().getQuantity());
	}

	// @Then("^The (.+) side of the order book should look like this at the end of the trade:$")
	// public void verifyOrderBookState(OrderSide side, int quantity, double
	// price) {
	// if(side == OrderSide.BUY) {
	// assertEquals(limitOrders, orderbook.getBuyOrders());
	// }
	// else {
	// assertEquals(limitOrders, orderbook.getSellOrders());
	// }
	// }
}
