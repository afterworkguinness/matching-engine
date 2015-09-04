package com.infusion.trading.matching.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LimitOrder implements Order {

	private int quantity;
	private double limitPrice;
	private OrderSide side;
	private boolean completed=false;
	private long arrivalTimeInOrderBook;
	private int quantityOfLastTransaction;
	private boolean holdInStaging;
	private boolean partialFillsAllowed = true;
	private String symbol;

	private Logger LOGGER = LoggerFactory.getLogger(com.infusion.trading.matching.domain.LimitOrder.class);

	public LimitOrder(int quantity, double limitPrice, OrderSide side) {
		this.quantity = quantity;
		this.limitPrice = limitPrice;
		this.side = side;
		this.partialFillsAllowed=true;
	}
	public LimitOrder(String symbol, int quantity, double limitPrice, OrderSide side) {
		this.quantity = quantity;
		this.limitPrice = limitPrice;
		this.side = side;
		this.partialFillsAllowed=true;
		this.symbol=symbol;
	}

	public LimitOrder(int quantity, double limitPrice, OrderSide side, OrderDesignation designation) {
		this.quantity = quantity;
		this.limitPrice = limitPrice;
		this.side = side;
		this.partialFillsAllowed = (designation == null);
	}

	@Override
	public void reduceRemainingQuantity(int transactionQuantity) {
		quantity -= transactionQuantity;
		quantityOfLastTransaction = transactionQuantity;
		if (quantity == 0) {
			completed = true;
		}
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(double limitPrice) {
		this.limitPrice = limitPrice;
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

	public long getArrivalTimeInOrderBook() {
		return arrivalTimeInOrderBook;
	}

	public void setArrivalTimeInOrderBook(long arrivalTimeInOrderBook) {

		if (arrivalTimeInOrderBook == 0) {

			this.arrivalTimeInOrderBook = arrivalTimeInOrderBook;
		}
		else {
			throw new UnsupportedOperationException("Arrival time in orderbook can only be set once!");
		}
	}

	@Override
	public boolean equals(Object objectToTest) {

		if (objectToTest instanceof LimitOrder) {
			LimitOrder orderToTest = (LimitOrder) objectToTest;
			if (orderToTest.getSide() == getSide() &&
					orderToTest.getLimitPrice() == getLimitPrice() && 
					orderToTest.getQuantity() == getQuantity()
					&& orderToTest.getArrivalTimeInOrderBook() == getArrivalTimeInOrderBook()
					&& orderToTest.isCompleted() == isCompleted()) {
					//FIXME: Why does adding this break everything ??? 
//					&& orderToTest.isPartialFillsAllowed() == partialFillsAllowed) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Double.hashCode(getLimitPrice()) + getQuantity() + Long.hashCode(getArrivalTimeInOrderBook());
	}

	@Override
	public String toString() {

		return "[Limit Order]: Side: " + side.name() + " | Quantity: " + quantity + " | Limit Price: " + limitPrice + " | Partial Fills Allowed: " + partialFillsAllowed ;
	}

	@Override
	public void setLastTradedPrice(double price) {
		// do nothing for limit orders
	}

	@Override
	public double getLastTradedPrice() {
		throw new UnsupportedOperationException("Not supported by limit orders");
	}

	@Override
	public int getQuantityOfLastTransaction() {
		return this.quantityOfLastTransaction;
	}

	public boolean isHoldInStaging() {
		return holdInStaging;
	}

	public void holdInStaging() {
		this.holdInStaging = true;
	}
	
	public void reset() {
		holdInStaging = false;
		completed=false;
		quantity += quantityOfLastTransaction;
	}

	@Override
	public boolean isPartialFillsAllowed() {
		return partialFillsAllowed;
	}

	@Override
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
