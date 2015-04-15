package com.infusion.trading.matching.lmit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.orderbook.IOrderArrivalTimeService;

@Component
public class LimitOrderFactory {

	@Autowired
	private IOrderArrivalTimeService arrivalTimeService;

	public LimitOrder createLimitOrder(LimitOrderDetails orderDetails) {

		return new LimitOrder(orderDetails, arrivalTimeService.getArrivalTimeInOrderBook());
	}
}
