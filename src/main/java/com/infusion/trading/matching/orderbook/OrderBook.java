package com.infusion.trading.matching.orderbook;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.algo.IOrderPlacementAlgorithm;
import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.OrderSide;

@Component
public class OrderBook {

	@Autowired
	private IOrderArrivalTimeService arrivalTimeService;

	@Autowired
	private IOrderPlacementAlgorithm orderPlacementAlgorithm;

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
			LinkedList<LimitOrder> orders = null;
			switch (order.getSide()) {
			case BUY:
				orders = (LinkedList<LimitOrder>) buyOrders;
				break;
			case SELL:
				orders = (LinkedList<LimitOrder>) sellOrders;
				break;
			}

			int position = orderPlacementAlgorithm.findPositionToPlaceInBook(orders, order);

			if (position == 0) {
				orders.addFirst(order);
			} else if (position == -1) {
				orders.addLast(order);
			} else {
				orders.add(position, order);
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
		} else {
			orders = sellOrders;
		}

		int start = TOP;

		while (orders.isEmpty() == false && start < orders.size()) {

			order = orders.get(start);

			if (order.isHoldInStaging() == false) {
				return order;
			}
			start++;
		}
		return null;
	}

	public void removeCompletedOrder(OrderSide side, boolean incomingOrderAllowsPartialFills) {
		// will only ever remove starting at top

		/*
		 * Want to lock the entire order book at one time. Only one order,
		 * regardless if it's buy or sell allowed in at one time
		 */

		synchronized (this) {
			switch (side) {
			case BUY:
				if (incomingOrderAllowsPartialFills == false) {
					buyOrders.get(0).holdInStaging();
				} else {
					buyOrders.remove(0);
				}
				break;
			case SELL:
				if (incomingOrderAllowsPartialFills == false) {
					sellOrders.get(0).holdInStaging();
				} else {
					sellOrders.remove(0);
				}
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
		return false;
	}

	public void revertStagedOrders(OrderSide side) {
		List<LimitOrder> orders = null;

		switch (side) {
		case BUY:
			orders = buyOrders;
		case SELL:
			orders = sellOrders;
		}

		for (LimitOrder order : orders) {

			if (order.isHoldInStaging()) {
				order.reset();
			}
		}
	}

	public void completeStagedOrders(OrderSide side) {
		List<LimitOrder> orders = null;

		switch (side) {
		case BUY:
			orders = buyOrders;
		case SELL:
			orders = sellOrders;
		}
		
		int lastIndexToRemove=-1;
		
		// This mess is necessary to avoid concurrent modification exception
		
		for (int i=0; i< orders.size(); i++) {

			if (orders.get(i).isHoldInStaging()) {
				lastIndexToRemove=i;
			}
		}
		
		for(int i =0; i <= lastIndexToRemove; i++) {
			orders.remove(i);
		}
	}
}
