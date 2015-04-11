package com.infusion.trading.matching.domain;

public class MarketOrder implements Order {

	private int quantity;
	private OrderSide side;
	
	public MarketOrder(OrderSide side, int quantity) {
		this.quantity=quantity;
		this.side=side;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
