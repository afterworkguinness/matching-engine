package com.infusion.trading.matching.domain;

import org.springframework.beans.factory.annotation.Autowired;

import com.infusion.trading.matching.orderbook.IOrderArrivalTimeService;


public class LimitOrder implements Order {
	
	private OrderSide side;
	private int quantity;
	private double limitPrice;
	private boolean completed=false;

	/*
	 * TODO: 
	 * Need to mock arrival time in tests. In cucumber steps use mockito to always return 0
	 */
	@Autowired
	private IOrderArrivalTimeService arrivalTimeService;
	
	public long getArrivalTime() {
		return arrivalTimeService.getArrivalTimeInOrderBook();
	}

	public LimitOrder() {
		//No arg constructor needed for Cucumber
		arrivalTimeService.recordArrivalTimeInOrderBook();
	}

	/*
	 * Need to use arrivalTimeService when order is created but can't do in constructor. 
	 * Static init method ???
	 */
	public LimitOrder(int quanity, double limitPrice, OrderSide side) {
		//CANNOT use autowired field in the constructor!!!
		arrivalTimeService.recordArrivalTimeInOrderBook();
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
	
	public boolean isCompleted() {
		return completed;
	}
	
	@Override
	public boolean equals(Object objectToCompare) {
		if(objectToCompare instanceof LimitOrder) {
			LimitOrder limitOrderToCompare = (LimitOrder)objectToCompare;
			if(limitOrderToCompare.getLimitPrice() == getLimitPrice() &&
			   limitOrderToCompare.getQuantity() == getQuantity() &&
			   limitOrderToCompare.getSide() == getSide() &&
			   limitOrderToCompare.getArrivalTime() == getArrivalTime()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(getLimitPrice()) +
				Double.hashCode(getQuantity()) +
				getSide().hashCode() +
				Long.hashCode(getArrivalTime());
	}

	@Override
	public String toString() {
		return "LimitOrder [side=" + side + ", quantity=" + quantity + ", limitPrice=" + limitPrice + ", completed=" + completed + ", arrivalTime="
				+ arrivalTimeService.getArrivalTimeInOrderBook() + "]";
	}
}
