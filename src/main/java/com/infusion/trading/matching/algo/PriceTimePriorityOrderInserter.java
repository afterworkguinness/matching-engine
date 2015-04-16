package com.infusion.trading.matching.algo;

import java.util.List;

import org.springframework.stereotype.Component;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.OrderSide;

@Component
public class PriceTimePriorityOrderInserter implements IOrderPlacementAlgorithm {

	public int findPositionToPlaceInBook(List<LimitOrder> orders, LimitOrder order) {

		int position = -1;

		if (orders.isEmpty() == false) {
			for (int i = 0; i < orders.size(); i++) {
				if (compare(order, orders.get(i))) {
					position = i;
					break;
				}
				else if (order.getLimitPrice() == orders.get(i).getLimitPrice()) {
					/*
					 * If the order matches the price of an existing order,
					 * place it after the existing order
					 */
					position = i + 1;
					break;
				}
			}
		}
		else {
			position = 0;
		}
		return position;
	}

	private boolean compare(LimitOrder left, LimitOrder right) {

		// Buy orders are ranked price descending
		// Sell orders are ranked price ascending

		if (left.getSide() == OrderSide.BUY) {
			return left.getLimitPrice() > right.getLimitPrice();
		}
		else {
			return left.getLimitPrice() < right.getLimitPrice();
		}
	}
}
