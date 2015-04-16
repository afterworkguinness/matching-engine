package com.infusion.trading.matching.orderbook;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.OrderSide;

@Component
public class OrderBook {

	@Autowired
	private IOrderArrivalTimeService arrivalTimeService;

	private static List<LimitOrder> buyOrders = new LinkedList<LimitOrder>();
	private static List<LimitOrder> sellOrders = new LinkedList<LimitOrder>();
	private final int TOP = 0;

	public void addLimitOrder(LimitOrder order) {

		order.setArrivalTimeInOrderBook(arrivalTimeService.getArrivalTimeInOrderBook());
		/*
		 * Want to lock the entire order book at one time. Only one order,
		 * regardless if it's buy or sell allowed in at one time
		 */
		synchronized (this) {

			switch (order.getSide()) {
				case BUY:
					buyOrders.add(order);
					break;
				case SELL:
					sellOrders.add(order);
					break;
			}

			/*
			 * Backup to DB while still locking. If you do it after lock is
			 * released, it could be stale
			 */
		}
	}

	public LimitOrder retrieveOrder(OrderSide side) {
		LimitOrder order = null;
		List<LimitOrder> orders;

		if (side == OrderSide.BUY) {
			orders = buyOrders;
		}
		else {
			orders = sellOrders;
		}

		if (orders.isEmpty() == false) {
			order = orders.get(TOP);
		}
		return order;

	}

	public void removeCompletedOrder(OrderSide side) {
		// will only ever remove starting at top

		/*
		 * Want to lock the entire order book at one time. Only one order,
		 * regardless if it's buy or sell allowed in at one time
		 */

		synchronized (this) {
			switch (side) {
				case BUY:
					buyOrders.remove(0);
					break;
				case SELL:
					sellOrders.remove(0);
					break;

			/*
			 * Backup to DB while still locking. If you do it after lock is
			 * released, it could be stale
			 */
			}
		}
	}

	/*
	 * Think these through. Maybe need to synchronize and return them as a
	 * Pair()
	 */
	public List<LimitOrder> getBuyOrders() {
		return Collections.unmodifiableList(buyOrders);
	}

	public List<LimitOrder> getSellOrders() {
		return Collections.unmodifiableList(sellOrders);
	}

	/*
	 * ONLY FOR TESTING! Find a way to get rid of this
	 */
	public void clear() {
		sellOrders.clear();
		buyOrders.clear();
	}

	public boolean isLiquidityLeft(OrderSide side) {
		switch (side) {
			case BUY:
				return buyOrders.isEmpty() == false;
			case SELL:
				return sellOrders.isEmpty() == false;
		}
		;
		return false;
	}
}
