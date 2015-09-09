package com.infusion.trading.matching.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/trade")
public class TradeEndpoint {

	@GET
	public String getFoo() {
		return "Foo";
	}
}
