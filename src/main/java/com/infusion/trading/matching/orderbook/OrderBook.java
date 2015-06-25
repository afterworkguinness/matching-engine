package com.infusion.trading.matching.orderbook;

import java.util.ArrayList;
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

	public List<LimitOrder> getStagedOrders(OrderSide side) {
		List<LimitOrder> orders = getOrders(side);
		List<LimitOrder> stagedOrders = new ArrayList<LimitOrder>();
		for (LimitOrder order : orders) {
			if (order.isHoldInStaging()) {
				stagedOrders.add(order);
			}
		}
		return Collections.<LimitOrder> unmodifiableList(stagedOrders);
	}

	public void addLimitOrder(LimitOrder order) {

		order.setArrivalTimeInOrderBook(arrivalTimeService.getArrivalTimeInOrderBook());

		/*
		 * Want to lock the entire order book at one time. Only one order,
		 * regardless if it's buy or sell allowed in at one time
		 */
		synchronized (this) {

			LinkedList<LimitOrder> orders = (LinkedList<LimitOrder>) getOrders(order.getSide());

			int position = orderPlacementAlgorithm.findPositionToPlaceInBook(orders, order);

			if (position == 0) {
				orders.addFirst(order);
			}
			else if (position == -1) {
				orders.addLast(order);
			}
			else {
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
		List<LimitOrder> orders = getOrders(side);

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

			List<LimitOrder> orders = getOrders(side);

			if (incomingOrderAllowsPartialFills == false) {
				orders.get(TOP).holdInStaging();
			}
			else {
				orders.remove(TOP);
			}

			/*
			 * Backup to DB while still locking. If you do it after lock is
			 * released, it could be stale
			 */
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

		OrderSide sideToCheck = side.getOppositeSide();
		return getOrders(sideToCheck).isEmpty() == false;
	}

	public void revertStagedOrders(OrderSide side) {

		List<LimitOrder> orders = getOrders(side);

		for (LimitOrder order : orders) {

			if (order.isHoldInStaging()) {
				order.reset();
			}
		}
	}

	public void completeStagedOrders(OrderSide side) {

		List<LimitOrder> orders = getOrders(side);

		int lastIndexToRemove = -1;

		// This mess is necessary to avoid concurrent modification exception

		for (int i = 0; i < orders.size(); i++) {

			if (orders.get(i).isHoldInStaging()) {
				lastIndexToRemove = i;
			}
		}

		for (int i = 0; i <= lastIndexToRemove; i++) {
			orders.remove(i);
		}
	}

	private List<LimitOrder> getOrders(OrderSide side) {

		switch (side) {
			case BUY:
				return buyOrders;
			case SELL:
				return sellOrders;
			default:
				return null;
		}
	}
}
