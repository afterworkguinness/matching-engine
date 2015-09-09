package com.infusion.trading.matching.execution;

/*
 * Will need to expand this to include:
 * Buy & sell trade IDs
 * Stock
 * timestamp
 */
public class Transaction {
	
	private double tradePrice;
	private int quantity;
	private String symbol;
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	Transaction(String symbol, double tradePrice, int quantity) {
		this.tradePrice = tradePrice;
		this.quantity = quantity;
		this.symbol = symbol;
	}

	public int getQuantity() {
		return quantity;
	}


	public double getTradePrice() {
		return tradePrice;
	}
	
	@Override
	public boolean equals(Object objectToTest) {
		if(objectToTest instanceof Transaction) {
			Transaction transactionToTest = (Transaction)objectToTest;
			
			if(transactionToTest.getTradePrice() == getTradePrice() &&
					transactionToTest.getQuantity() == getQuantity()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Transaction [tradePrice=" + tradePrice + ", quantity=" + quantity + "]";
	}
}