package com.infusion.trading.matching.web.domain;

public class LimitOrderModel {

	private String symbol;
	private Double limitPrice;
	private Integer quantity;

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

	@Override
	public String toString() {
		return "LimitOrderModel [symbol=" + symbol + ", limitPrice=" + limitPrice + ", quantity=" + quantity + "]";
	}

}
