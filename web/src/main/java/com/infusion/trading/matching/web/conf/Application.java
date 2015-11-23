package com.infusion.trading.matching.web.conf;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.infusion.trading.matching.web.endpoint.TradeEndpoint;
import org.glassfish.jersey.server.ServerProperties;

import java.util.HashMap;
import java.util.Map;

public class Application extends ResourceConfig {

	public Application () {
		packages("com.infusion.trading.matching.web.endpoint");
		register(JacksonFeature.class);
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, "true");
	}

}
