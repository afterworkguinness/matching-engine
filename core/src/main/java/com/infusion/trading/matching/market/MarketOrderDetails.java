package com.infusion.trading.matching.market;

import com.infusion.trading.matching.domain.OrderSide;

public class MarketOrderDetails {

	private int quantity;
	private OrderSide side;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public OrderSide getSide() {
		return side;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}
}
