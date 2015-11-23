package com.infusion.trading.matching.web.domain;

import com.infusion.trading.matching.domain.Order;
import com.infusion.trading.matching.domain.OrderDesignation;
import com.infusion.trading.matching.domain.OrderSide;

import javax.validation.constraints.NotNull;

public class LimitOrderModel {

	@NotNull
	private String symbol;
	@NotNull
	private Double limitPrice;
	@NotNull
	private Integer quantity;
	@NotNull
	private OrderSide side;
	private OrderDesignation designation;

	public OrderDesignation getDesignation() { return this.designation; }

	public void setDesignation(OrderDesignation designation) {this.designation = designation;}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Double getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(Double limitPrice) {
		this.limitPrice = limitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setSide(OrderSide side) {

		this.side = side;
	}

	public OrderSide getSide() {

		return side;
	}

	@Override
	public String toString() {
		return "LimitOrderModel [symbol=" + symbol + ", limitPrice=" + limitPrice + ", quantity=" + quantity + "]";
	}

}
