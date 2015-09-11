package com.infusion.trading.matching.web.endpoint;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.util.UUID;
import com.infusion.trading.matching.bootstrap.Bootstrapper;
import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.domain.MarketOrder;
import com.infusion.trading.matching.matcher.OrderFillService;
import com.infusion.trading.matching.web.domain.LimitOrderModel;
import com.infusion.trading.matching.web.domain.MarketOrderModel;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/trade")
public class TradeEndpoint {
	// TODO: make this configurable!!
	private ExecutorService threadPool = Executors.newFixedThreadPool(10);

	@Autowired
	private OrderFillService orderFillService;
	
	@POST
	@Path("limit")
	@Consumes({ MediaType.APPLICATION_JSON })
	public void limitOrder(LimitOrderModel limitOrder) {

		threadPool.submit(createWorkItem(limitOrder));
	}

	@POST
	@Path("market")
	@Consumes({MediaType.APPLICATION_JSON})
	public void marketOrder(MarketOrderModel marketOrder) {

		//Core currently has a bug that you can't submit a market order if the book is empty.
		threadPool.submit(createWorkItem(marketOrder));
	}

	private Runnable createWorkItem(final LimitOrderModel model) {

		Runnable workItem = new Runnable(){

			public void run() {
				orderFillService.attemptToFillOrder(transform(model));
			}
		};

		return workItem;
	}

	public Runnable createWorkItem (MarketOrderModel model) {

		Runnable workItem = new Runnable(){

			public void run() {
				orderFillService.attemptToFillOrder(transformMarketOrderModel(model));
			}
		};

		return workItem;
	}

	private MarketOrder transformMarketOrderModel(MarketOrderModel model) {
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