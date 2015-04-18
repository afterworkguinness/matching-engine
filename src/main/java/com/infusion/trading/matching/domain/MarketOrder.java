package com.infusion.trading.matching.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarketOrder implements Order {

	private int quantity;
	private OrderSide side;
	private boolean completed = false;
	private double totalPrice;
	private double lastTradedPrice;

	private Logger LOGGER = LoggerFactory.getLogger(com.infusion.trading.matching.domain.MarketOrder.class);

	public MarketOrder(OrderSide side, int orderQuantity) {
		this.quantity = orderQuantity;
		this.side = side;
	}

	@Override
	public void reduceRemainingQuantity(int transactionQuantity) {
		quantity -= transactionQuantity;
		if (quantity == 0) {
			completed = true;
		}
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int orderQuantity) {
		this.quantity = orderQuantity;
	}

	@Override
	public OrderSide getSide() {
		return side;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}

	@Override
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

	@Override
	public double getLastTradedPrice() {
		return lastTradedPrice;
	}

	@Override
	public void setLastTradedPrice(double lastTradedPrice) {
		this.lastTradedPrice = lastTradedPrice;
	}

	@Override
	public String toString() {

		return "[Market Order]: Side: " + side.name() + " | Quantity: " + quantity + " | Last Traded Price: " + lastTradedPrice;
	}
}
