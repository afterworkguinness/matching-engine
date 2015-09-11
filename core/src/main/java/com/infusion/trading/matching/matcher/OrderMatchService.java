package com.infusion.trading.matching.matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.MarketOrder;
import com.infusion.trading.matching.domain.Order;
import com.infusion.trading.matching.domain.OrderSide;
import com.infusion.trading.matching.orderbook.OrderBook;
import com.infusion.trading.matching.orderbook.OrderBookService;

@Component
public class OrderMatchService {

	@Autowired
	private OrderBookService orderBookService;

	private Logger LOGGER = LoggerFactory.getLogger(OrderMatchService.class);

	public LimitOrder findMatchingOrder(Order order) {
		OrderBook orderBook = orderBookService.getOrderBook(order.getSymbol());
		LOGGER.trace("Orderbook selected: " + orderBook);
		LimitOrder orderAtTopOfBook = orderBook.retrieveOrder(order.getSide().getOppositeSide());
		LOGGER.trace("Order at top of book: " + orderAtTopOfBook);

		/* Liquidity is checked before calling this method, so if it returns
		* null that's b/c a it doesn't match the limit orders price limit
		*/
		if (order instanceof MarketOrder || orderAtTopOfBook == null) {
			return orderAtTopOfBook;
		}

		LimitOrder limitOrder = (LimitOrder) order;

		if (OrderSide.BUY == order.getSide()) {

			if (limitOrder.getLimitPrice() >= orderAtTopOfBook.getLimitPrice()) {
				return orderAtTopOfBook;
			}
		}
		else if (OrderSide.SELL == order.getSide()) {

			if (limitOrder.getLimitPrice() <= orderAtTopOfBook.getLimitPrice()) {
				return orderAtTopOfBook;
			}
		}
		return null;
	}
}
