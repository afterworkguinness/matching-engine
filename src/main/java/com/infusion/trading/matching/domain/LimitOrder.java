package com.infusion.trading.matching.domain;

public class LimitOrder implements Order {
	
	private int quantity;
	private int limitPrice;

	public void setQuantity(int quantity) {

		this.quantity=quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(int limitPrice) {
		this.limitPrice = limitPrice;
	}
}
