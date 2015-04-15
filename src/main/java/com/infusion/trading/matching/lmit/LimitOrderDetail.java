package com.infusion.trading.matching.lmit;

import com.infusion.trading.matching.domain.OrderSide;

/**
 * 
 * @author paul
 * 
 *         The broker's Direct Market Access system will send this object to the
 *         matching engine. The matching engine will embed it in a LimitOrder
 *         which has the additional fields creationTime, and completed The
 *         creation time is set when it is processed by the matching engine, not
 *         when the broker's system creates it
 *
 */
public class LimitOrderDetail {

	private OrderSide side;
	private int quantity;
	private double limitPrice;

	public LimitOrderDetail() {
		// For Cucumber
	}

	public LimitOrderDetail(int quantity, double limitPrice, OrderSide side) {
		this.quantity = quantity;
		this.limitPrice = limitPrice;
		this.side = side;
	}

	public OrderSide getSide() {
		return side;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}

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
	public boolean equals(Object objectToTest) {
		if (objectToTest instanceof LimitOrderDetail) {
			LimitOrderDetail detailsToTest = (LimitOrderDetail) objectToTest;

			if (detailsToTest.getLimitPrice() == getLimitPrice() && detailsToTest.getQuantity() == getQuantity() && detailsToTest.getSide() == getSide()) {
				return true;
			}
		}
		return false;
	}
}
