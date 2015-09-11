package com.infusion.trading.matching.web.conf;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.infusion.trading.matching.web.endpoint.TradeEndpoint;

public class Application extends ResourceConfig {

	public Application () {
		packages("com.infusion.trading.matching.web.endpoint");
		register(JacksonFeature.class);
		//		property("async-supported", "true");
	}
}
