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

import com.infusion.trading.matching.bootstrap.Bootstrapper;
import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.matcher.OrderFillService;
import com.infusion.trading.matching.web.domain.LimitOrderModel;

@Path("/trade")
public class TradeEndpoint {
	// TODO: make this configurable!!
	private ExecutorService threadPool = Executors.newFixedThreadPool(10);

	private static OrderFillService orderFillService;
	
	static {
		orderFillService = Bootstrapper.getOrderFillService();
	}

	@GET
	public String getFoo() {
		return "Foo";
	}

	@POST
	@Path("limit")
	@Consumes({ MediaType.APPLICATION_JSON })
	public void limitOrder(LimitOrderModel limitOrder) {
		System.out.println(limitOrder);

		threadPool.submit(createWorkItem(limitOrder));
		System.out.println("SUBMITTED");
	}

	private Runnable createWorkItem(final LimitOrderModel model) {
		Runnable workItem = new Runnable(){

			public void run() {
				orderFillService.attemptToFillOrder(transform(model));
				System.out.println("DONE");
			}
		};

		return workItem;
	}


	private LimitOrder transform(LimitOrderModel model) {
		LimitOrder order = new LimitOrder(model.getSymbol(), model.getQuantity(), model.getLimitPrice(), model.getSide());
		return order;
	}
}