package com.infusion.trading.matching.domain;

public class MarketOrder implements Order {

	private int quantity;
	private OrderSide side;
	private boolean completed = false;
	private double totalPrice;
	
	public MarketOrder(OrderSide side, int orderQuantity) {
		this.quantity=orderQuantity;
		this.side=side;
	}
	
	
	public void fill(LimitOrder limitOrder) {
		
		int transactionQuantity=Math.min(quantity, limitOrder.getQuantity());
		totalPrice += (transactionQuantity*limitOrder.getLimitPrice());
	}

	public void reduceRemainingQuantity(int transactionQuantity) {
		quantity-=transactionQuantity;
		
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
}
