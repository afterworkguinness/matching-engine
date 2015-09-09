package com.infusion.trading.matching.domain;

public enum OrderSide {

	BUY {
		
		@Override
		public OrderSide getOppositeSide() {
			return SELL;
		}

	},
	SELL {
		
		@Override
		public OrderSide getOppositeSide() {
			return BUY;
		}
	};
	public abstract OrderSide getOppositeSide();
}
