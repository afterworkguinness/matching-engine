package com.infusion.trading.matching.algo;

import java.util.List;

import com.infusion.trading.matching.domain.LimitOrder;

public interface IOrderPlacementAlgorithm {

	public int findPositionToPlaceInBook(List<LimitOrder> orders, LimitOrder order);
}
