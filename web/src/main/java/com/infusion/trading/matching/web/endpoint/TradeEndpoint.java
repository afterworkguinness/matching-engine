package com.infusion.trading.matching.endpoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.infusion.trading.matching.domain.LimitOrderModel;

@Path("/trade")
public class TradeEndpoint {

	@GET
	public String getFoo() {
		return "Foo";
	}

	@POST
	@Path("limit")
	@Consumes({ MediaType.APPLICATION_JSON })
	public void limitOrder(LimitOrderModel limitOrder) {
		System.out.println(limitOrder);
	}
}
