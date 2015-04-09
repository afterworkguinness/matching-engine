package com.infusion.trading.matching.domain;

public class MarketOrder implements Order {

	private int quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
