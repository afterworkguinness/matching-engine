package com.infusion.trading.matching.web.endpoint;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.MarketOrder;
import com.infusion.trading.matching.domain.Order;
import com.infusion.trading.matching.matcher.OrderFillService;
import com.infusion.trading.matching.web.domain.LimitOrderModel;
import com.infusion.trading.matching.web.domain.MarketOrderModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/trade")
public class TradeEndpoint {
	// TODO: make this configurable!!
	private ExecutorService threadPool = Executors.newFixedThreadPool(10);
	private Logger LOGGER = LoggerFactory.getLogger(TradeEndpoint.class);

	@Autowired
	private OrderFillService orderFillService;
	
	@POST
	@Path("limit")
	@Consumes({ MediaType.APPLICATION_JSON })
	public void limitOrder(LimitOrderModel limitOrder) {

		placeTrade(limitOrder);
	}

	@POST
	@Path("market")
	@Consumes({MediaType.APPLICATION_JSON})
	public void marketOrder(MarketOrderModel marketOrder) {

		//Core currently has a bug that you can't submit a market order if the book is empty.
		placeTrade(marketOrder);
	}

	private void placeTrade(LimitOrderModel model) {
		LimitOrder order = transform(model);
		placeTrade(order);
	}

	private void placeTrade(MarketOrderModel model) {
		MarketOrder order = transform(model);
		placeTrade(order);
	}

	private void placeTrade(final Order order) {
		MDC.put("TRADEID", order.getTradeID());
		orderFillService.placeTrade(order);
	}

	private MarketOrder transform(MarketOrderModel model) {
		MarketOrder order = new MarketOrder(model.getSymbol(), model.getSide(), model.getQuantity(), model.getDesignation());
		order.setTradeID((Long.toString(getUniqueTradeId())));
		return order;
	}


	private LimitOrder transform(LimitOrderModel model) {
		LimitOrder order = new LimitOrder(model.getSymbol(), model.getQuantity(), model.getLimitPrice(), model.getSide(), model.getDesignation());
		order.setTradeID(Long.toString(getUniqueTradeId()));
		return order;
	}

	private synchronized long getUniqueTradeId() {
		return System.currentTimeMillis();
	}
}