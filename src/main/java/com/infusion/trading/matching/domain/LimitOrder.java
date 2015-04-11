package com.infusion.trading.matching.domain;

public class LimitOrder implements Order {
	
	private int quantity;
	private double limitPrice;
	private OrderSide side;

	public LimitOrder(int quanity, double limitPrice, OrderSide side) {
		this.quantity=quanity;
		this.limitPrice=limitPrice;
		this.side = side;
	}
	
	public void setQuantity(int quantity) {

		this.quantity=quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(int limitPrice) {
		this.limitPrice = limitPrice;
	}
	
	public OrderSide getSide() {
		return side;
	}
}
