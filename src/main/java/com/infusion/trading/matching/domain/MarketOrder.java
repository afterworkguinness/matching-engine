package com.infusion.trading.matching.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarketOrder implements Order {

	private Integer quantity;
	private OrderSide side;
	private boolean completed = false;
	private Double totalPrice;
	private Double lastTradedPrice;
	private boolean partialFillsAllowed = true;
	private String symbol; 

	private Logger LOGGER = LoggerFactory.getLogger(com.infusion.trading.matching.domain.MarketOrder.class);

	public MarketOrder(String symbol, OrderSide side, int orderQuantity) {
		this.quantity = orderQuantity;
		this.side = side;
		this.symbol=symbol;
	}
	@Deprecated
	public MarketOrder(OrderSide side, int orderQuantity) {
		this.quantity = orderQuantity;
		this.side = side;
	}

	public MarketOrder(OrderSide side, int orderQuantity, OrderDesignation designation) {
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

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
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

		return "[Market Order]: Side: " + side.name() + " | Quantity: " + quantity + " | Last Traded Price: " + lastTradedPrice;
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
}
