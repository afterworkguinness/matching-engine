package com.infusion.trading.matching.domain;

public class LimitOrder implements Order {
	
	private int quantity;
	private double limitPrice;
	private OrderSide side;
	private boolean completed=false;

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
	
	public void reduceRemainingQuantity(int transactionQuantity) {
		quantity-=transactionQuantity;
		
		if (quantity == 0) {
			completed = true;
		}
	}
	
	public void fill(MarketOrder order) {
		quantity = Math.max(0, quantity-order.getQuantity());
		if(quantity == 0) {
			completed=true;
		}
	}
	
	public boolean isCompleted() {
		return completed;
	}
}
