package com.infusion.trading.matching.execution;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.Order;

public class Transaction {
	private Order order;
	private LimitOrder match;
	private double tradePrice;

	Transaction(Order order, LimitOrder match, double tradePrice) {
		this.order = order;
		this.match = match;
		this.tradePrice = tradePrice;
	}

	public Order getOrder() {
		return order;
	}

	public LimitOrder getMatch() {
		return match;
	}

	public double getTradePrice() {
		return tradePrice;
	}
	
	@Override
	public String toString() {
		return "Order: ["+order+"] Match: ["+match+"] Trade Price: ["+tradePrice+"]";
	}
	
	@Override
	public boolean equals(Object objectToTest) {
		if( objectToTest instanceof Transaction) {
			Transaction transactionToTest = (Transaction)objectToTest;
			
			if(transactionToTest.getMatch().equals(match) &&
					transactionToTest.getOrder().equals(order) &&
					transactionToTest.getTradePrice() == tradePrice) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		
		return order.hashCode() + match.hashCode() + new Double(tradePrice).hashCode();
	}
}