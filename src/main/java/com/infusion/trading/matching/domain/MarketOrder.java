package com.infusion.trading.matching.domain;

import com.infusion.trading.matching.lmit.LimitOrder;

public class MarketOrder implements Order {

	private int quantity;
	private OrderSide side;
	private boolean completed = false;
	private double totalPrice;
	private double lastTradedPrice;

	public MarketOrder(OrderSide side, int orderQuantity) {
		this.quantity = orderQuantity;
		this.side = side;
	}

	public void fill(LimitOrder limitOrder) {

		int transactionQuantity = Math.min(quantity, limitOrder.getOrderDetail().getQuantity());
		totalPrice += (transactionQuantity * limitOrder.getOrderDetail().getLimitPrice());
		lastTradedPrice = limitOrder.getOrderDetail().getLimitPrice();
	}

	public void reduceRemainingQuantity(int transactionQuantity) {
		quantity -= transactionQuantity;

		if (quantity == 0) {
			completed = true;
		}
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int orderQuantity) {
		this.quantity = orderQuantity;
	}

	public OrderSide getSide() {
		return side;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getLastTradedPrice() {
		return lastTradedPrice;
	}

	public void setLastTradedPrice(double lastTradedPrice) {
		this.lastTradedPrice = lastTradedPrice;
	}
}
