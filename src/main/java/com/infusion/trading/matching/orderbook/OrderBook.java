package com.infusion.trading.matching.orderbook;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.OrderSide;

public class OrderBook {

	private static List<LimitOrder>	buyOrders	= new LinkedList<LimitOrder>();
	private static List<LimitOrder>	sellOrders	= new LinkedList<LimitOrder>();
	private final int				TOP			= 0;

	public void addLimitOrder(LimitOrder order) {
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
			;

			/*
			 * Backup to DB while still locking. If you do it after lock is
			 * released, it could be stale
			 */
		}
	}

	public LimitOrder retrieveOrder(OrderSide side) {
		LimitOrder order;

		if (side == OrderSide.BUY) {
			order = buyOrders.get(TOP);
		}
		else {
			order = sellOrders.get(TOP);
		}
//		if (order != null) {
//			removeFilledOrderFromTopOfBook(side);
//		}
		return order;

	}

	public void fillOrder(int fillQuantity) {
		// will only ever fill starting at top

		/*
		 * Want to lock the entire order book at one time. Only one order,
		 * regardless if it's buy or sell allowed in at one time
		 */

		synchronized (this) {

		}
	}

	private void removeFilledOrderFromTopOfBook(OrderSide side) {
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
}
