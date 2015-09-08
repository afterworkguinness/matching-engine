package com.infusion.trading.matching.test.common;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.MarketOrder;
import com.infusion.trading.matching.domain.OrderDesignation;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.execution.MockTradeExecutionService;
import com.infusion.trading.matching.matcher.OrderFillService;
import com.infusion.trading.matching.orderbook.OrderBook;
import com.infusion.trading.matching.orderbook.TestOrderBookService;

@Component
public class TestHelper {

	@Autowired
	private TestOrderBookService orderBookService;

	@Autowired
	private OrderFillService orderFillService;

	@Autowired
	private MockTradeExecutionService tradeExecutionService;

	private Map<String, OrderBook> expectedOrderBooks = new HashMap<String, OrderBook>();

	
	public void setupOrderBook(List<LimitOrder> limitOrders) {
		
		expectedOrderBooks.clear();
		orderBookService.deleteAllOrderBooks();

		for (LimitOrder order : limitOrders) {
			OrderBook orderBook = orderBookService.getOrderBook(order.getSymbol());
			tradeExecutionService.reset();
			orderBook.addLimitOrder(order);
		}
	}

	public void fillNewLimitOrder(OrderSide side, int quantity, String symbol, double price, OrderDesignation  designation) {
		orderFillService.attemptToFillOrder(new LimitOrder(symbol, quantity, price, side, designation));
	}
	
	public void fillNewMarketOrder(OrderSide side, int quantity, String symbol, OrderDesignation designation) {
		orderFillService.attemptToFillOrder(new MarketOrder(symbol, side, quantity, designation));
	}
	
	public void verifyOrderBookState(List<LimitOrder> expectedLmitOrders) {

		for (LimitOrder expectedOrder : expectedLmitOrders) {

			String symbol = expectedOrder.getSymbol();
			OrderBook expectedOrderBook = retrievetOrCreateExpectedOrderBook(symbol);

			/* The test expected data shows and empty order book, but really each line is a limit order.
			 * If the book is expected to be empty, we need to make sure we don't try and add a null limit order to it 
			 */
			if (expectedOrder.getLimitPrice() != null) {
				expectedOrderBook.addLimitOrder(expectedOrder);
			}
		}

		assertEquals(expectedOrderBooks, orderBookService.getAllOrderBooks());
	}
	
	private OrderBook retrievetOrCreateExpectedOrderBook(String symbol) {
		OrderBook book = null;

		if (expectedOrderBooks.containsKey(symbol)) {
			book = expectedOrderBooks.get(symbol);
		} else {
			book = orderBookService.forceCreateNewOrderBook(symbol);
			expectedOrderBooks.put(symbol, book);
		}
		return book;
	}
}
