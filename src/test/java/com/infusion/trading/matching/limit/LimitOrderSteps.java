package com.infusion.trading.matching.limit;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.execution.MockTradeExecutionService;
import com.infusion.trading.matching.matcher.OrderFillService;
import com.infusion.trading.matching.orderbook.OrderBook;
import com.infusion.trading.matching.orderbook.TestOrderBookService;
import com.infusion.trading.matching.test.common.TesUtil;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LimitOrderSteps {

	@Autowired
	private TestOrderBookService orderBookService;

	@Autowired
	private OrderFillService orderFillService;

	@Autowired
	private MockTradeExecutionService tradeExecutionService;

	@Autowired
	private TesUtil testUtil;

	private Map<String, OrderBook> expectedOrderBooks = new HashMap<String, OrderBook>();

	private Logger LOGGER = LoggerFactory.getLogger(LimitOrderSteps.class);

	@Given("^The order book looks like this before the trade is placed:$")
	public void setupOrderbook(List<LimitOrder> limitOrders) {
		for (LimitOrder order : limitOrders) {
			OrderBook orderBook = testUtil.getOrderBook(order);
			orderBook.clear();
			tradeExecutionService.reset();
			orderBook.addLimitOrder(order);
		}
	}

	@When("^A limit (.+) order is placed for (\\d+) shares at (\\d+)$")
	public void addLimitOrder(OrderSide side, int quantity, double limitPrice) {
		orderFillService.attemptToFillOrder(new LimitOrder(quantity, limitPrice, side));
	}

	@When("^A limit (.+) order is placed for (\\d+) shares of (.+) at (\\d+)$")
	public void addLimitOrder(OrderSide side, int quantity, String symbol, double limitPrice) {
		orderFillService.attemptToFillOrder(new LimitOrder(symbol, quantity, limitPrice, side));
	}

	@Then("^The order book should look like this at the end of the trade:$")
	public void verifyOrderBookState(List<LimitOrder> expectedLmitOrders) {

		/*
		 * Create map of Symbol and OrderBook (just like in orderbook svc) For
		 * each order in expected: Add it to buy/sell side of orderbook for its
		 * symbol When done creating orderbooks for expected data, compare the
		 * expected orderbooks with the real ones
		 */

		for (LimitOrder expectedOrder : expectedLmitOrders) {

			String symbol = expectedOrder.getSymbol();
			OrderBook expectedOrderBook = retrievetOrCreateExpectedOrderBook(symbol);
			expectedOrderBook.addLimitOrder(expectedOrder);
		}

		OrderBook expectedOrderBook;
		OrderBook actualOrderBook;

		for (Map.Entry<String, OrderBook> entry : expectedOrderBooks.entrySet()) {
			String symbol = entry.getKey();
			expectedOrderBook = entry.getValue();
			actualOrderBook = orderBookService.getOrderBook(symbol);
			assertEquals("Testing order book for [" + symbol + "]", expectedOrderBook, actualOrderBook);
		}
	}

	private OrderBook retrievetOrCreateExpectedOrderBook(String symbol) {
		OrderBook book = null;

		if (expectedOrderBooks.containsKey(symbol)) {
			book = expectedOrderBooks.get(symbol);
		}
		else {
			book = orderBookService.forceCreateNewOrderBook();
			expectedOrderBooks.put(symbol, book);
		}
		return book;
	}

	@Then("^It crosses the bid ask spread and is executed at (\\d+)$")
	public void verifyCrossesTheSpread(double executionPrice) {

		/*
		 * Can't use Mockito to verify TradeExecutionService. To use Mockito
		 * verify, you must execute the code and do the verify right there With
		 * Cucumber, we are splitting up the execution and verification into two
		 * methods Must use a hand rolled stub
		 */
		double actualTradePrice = tradeExecutionService.getTransactions().get(0).getTradePrice();

		assertEquals(actualTradePrice, executionPrice, 0.00001);
	}
}
