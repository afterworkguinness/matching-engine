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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infusion.trading.matching.domain.LimitOrder;
import com.infusion.trading.matching.matcher.OrderFillService;
import com.infusion.trading.matching.web.domain.LimitOrderModel;

@Component
@Path("/trade")
public class TradeEndpoint {
	// TODO: make this configurable!!
	private ExecutorService threadPool = Executors.newFixedThreadPool(10);

	@Autowired
	private OrderFillService orderFillService;

	@GET
	public String getFoo() {
		return "Foo";
	}

	@POST
	@Path("limit")
	@Consumes({ MediaType.APPLICATION_JSON })
	public void limitOrder(@Suspended final AsyncResponse response, LimitOrderModel limitOrder) {
		System.out.println(limitOrder);
		threadPool.submit(createWorkItem(limitOrder));

	}

	private Callable<String> createWorkItem(final LimitOrderModel model) {
		Callable<String> callable = new Callable<String>() {

			@Override
			public String call() {
				orderFillService.attemptToFillOrder(transform(model));
				return "success"; // won't get hit if orderfillservice fails
			}
		};

		return callable;
	}

	private LimitOrder transform(LimitOrderModel model) {
		LimitOrder order = new LimitOrder(model.getSymbol(), model.getQuantity(), model.getLimitPrice(), null);
		return order;
	}
}
