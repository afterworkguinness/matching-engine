package com.infusion.trading.matching.lmit;

import com.infusion.trading.matching.domain.Order;

public class LimitOrder implements Order {

	private LimitOrderDetails orderDetails;
	private boolean completed;
	private long creationTime;

	public long getCreationTime() {
		return creationTime;
	}

	// Should only be called from LimitOrerFactory
	LimitOrder(LimitOrderDetails orderDetails, long creationTime) {
		this.orderDetails = orderDetails;
		this.creationTime = creationTime;
	}

	public LimitOrderDetails getOrderDetails() {
		return orderDetails;
	}

	public void reduceRemainingQuantity(int transactionQuantity) {
		orderDetails.setQuantity(orderDetails.getQuantity() - transactionQuantity);

		if (orderDetails.getQuantity() == 0) {
			completed = true;
		}
	}

	public boolean isCompleted() {
		return completed;
	}

	@Override
	public boolean equals(Object objectToCompare) {
		if (objectToCompare instanceof LimitOrder) {
			LimitOrder limitOrderToCompare = (LimitOrder) objectToCompare;

			// Orders are processed one at a time, so it is not possible for two
			// orders to have the same creation time
			// /**** EXCEPT IN A TEST WHERE ALL ORDERS HAVE TIME 0
			if (limitOrderToCompare.getCreationTime() == getCreationTime()) {

				if (limitOrderToCompare.getOrderDetails().equals(getOrderDetails())) {

				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(creationTime);
	}

	@Override
	public String toString() {
		return "LimitOrder [side=" + orderDetails.getSide().name() + ", quantity=" + orderDetails.getQuantity() + ", limitPrice=" + orderDetails.getLimitPrice() + ", completed="
				+ completed + ", creationTime=" + creationTime + "]";
	}
}
