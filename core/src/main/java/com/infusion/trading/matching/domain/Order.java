package com.infusion.trading.matching.domain;

public interface Order {

	public Integer getQuantity();

	public OrderSide getSide();

	public void reduceRemainingQuantity(Integer transactionQuantity);

	public boolean isCompleted();

	public void setLastTradedPrice(Double price);

	public Double getLastTradedPrice();

	public Integer getQuantityOfLastTransaction();

	public boolean isPartialFillsAllowed();

	public String getSymbol();

	public String getTradeID();
}
