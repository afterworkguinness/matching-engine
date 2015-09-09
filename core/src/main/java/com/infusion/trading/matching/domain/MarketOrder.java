package com.infusion.trading.matching.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarketOrder implements Order {

	private Integer quantity;
	private OrderSide side;
	private boolean completed = false;
	private Double lastTradedPrice;
	private boolean partialFillsAllowed = true;
	private String symbol;

	private Logger LOGGER = LoggerFactory.getLogger(com.infusion.trading.matching.domain.MarketOrder.class);

	public MarketOrder(String symbol, OrderSide side, int orderQuantity) {
		this.quantity = orderQuantity;
		this.side = side;
		this.symbol = symbol;
	}

	public MarketOrder(String symbol, OrderSide side, int orderQuantity, OrderDesignation designation) {
		this.symbol = symbol;
		this.quantity = orderQuantity;
		this.side = side;
		partialFillsAllowed = (designation == null);
	}

	@Override
	public void reduceRemainingQuantity(Integer transactionQuantity) {
		quantity -= transactionQuantity;
		if (quantity == 0) {
			completed = true;
		}
	}

	@Override
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(int orderQuantity) {
		this.quantity = orderQuantity;
	}

	@Override
	public OrderSide getSide() {
		return side;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}

	@Override
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public Double getLastTradedPrice() {
		return lastTradedPrice;
	}

	@Override
	public void setLastTradedPrice(Double lastTradedPrice) {
		this.lastTradedPrice = lastTradedPrice;
	}

	@Override
	public String toString() {

		return "[Market Order]: Symbol: " + symbol + " Side: " + side.name() + " | Quantity: " + quantity + " | Last Traded Price: " + lastTradedPrice;
	}

	@Override
	public Integer getQuantityOfLastTransaction() {
		throw new UnsupportedOperationException("Not supported for market orders");
	}

	@Override
	public boolean isPartialFillsAllowed() {
		return partialFillsAllowed;
	}

	@Override
	public String getSymbol() {
		return this.symbol;
	}

	@Override
	public boolean equals(Object objectToTest) {

		if (objectToTest instanceof MarketOrder) {
			MarketOrder marketOrderToTest = (MarketOrder) objectToTest;

			if (marketOrderToTest.getSymbol().equals(symbol) && 
					marketOrderToTest.getSide() == side &&
					marketOrderToTest.getQuantity().equals(quantity)
					&& marketOrderToTest.getLastTradedPrice().equals(lastTradedPrice)) {

				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return symbol.hashCode() + side.hashCode() + quantity + lastTradedPrice.hashCode();
	}
}
