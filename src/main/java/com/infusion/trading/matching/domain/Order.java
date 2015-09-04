package com.infusion.trading.matching.domain;

public interface Order {

	public int getQuantity();

	public OrderSide getSide();

	public void reduceRemainingQuantity(int transactionQuantity);

	public boolean isCompleted();

	public void setLastTradedPrice(double price);

	public double getLastTradedPrice();

	public int getQuantityOfLastTransaction();

	public boolean isPartialFillsAllowed();
	
	public String getSymbol();
}
