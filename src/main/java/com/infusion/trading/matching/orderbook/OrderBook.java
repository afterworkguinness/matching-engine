package com.infusion.trading.matching.orderbook;

import java.util.LinkedList;
import java.util.List;

import com.infusion.trading.matching.domain.LimitOrder;


public class OrderBook {
	
	private static List<LimitOrder> orders = new LinkedList<LimitOrder>();
	
	public static enum OrderBookSide {
		BUY,
		SELL
	};
	
	public void addLimitOrder(LimitOrder order) {
		/* Want to lock the entire order book at one time. 
		 * Only one order, regardless if it's buy or sell allowed in at one time
		 */
		
		synchronized(this) {
			orders.add(order);
			
			/* Backup to DB while still locking.
			 * If you do it after lock is released, it could be stale
			 */
		}
	}
	
	public void fillOrder(int fillQuantity) {
		//will only ever fill starting at top
		
		/* Want to lock the entire order book at one time. 
		 * Only one order, regardless if it's buy or sell allowed in at one time
		 */
		
		synchronized(this) {
			
		}
	}
	
	public void removeFilledOrderFromTopOfBook(OrderBookSide side) {
		//will only ever remove starting at top
		
		/* Want to lock the entire order book at one time. 
		 * Only one order, regardless if it's buy or sell allowed in at one time
		 */
		
		synchronized (this) {
			orders.remove(0);
			
			/* Backup to DB while still locking.
			 * If you do it after lock is released, it could be stale
			 */
		}
	}
}
