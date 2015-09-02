package com.infusion.trading.matching.execution;


public class Transaction {
	
	private double tradePrice;
	private int quantity;

	Transaction(double tradePrice, int quantity, long buyTradeId, long sellTradeId) {
		this.tradePrice = tradePrice;
		this.quantity = quantity;
	}


	public int getQuantity() {
		return quantity;
	}


	public double getTradePrice() {
		return tradePrice;
	}
}