package web;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infusion.trading.matching.domain.OrderDesignation;

public class TradeService {
	private Logger LOGGER = LoggerFactory.getLogger(TradeService.class);

	@POST
	@Path("/trade/market/")
	public void placeMarketOrderTrade(String symbol, int quantity) {

		LOGGER.debug("Market order trade placed: Symbol: " + symbol + " | Quantity: " + quantity);
	}

	public void placeLimitOrderTrade(String symbol, int quantity, double limitPrice) {

	}

	public void placeLimitOrderTrade(String symbol, int quantity, double limitPrice, OrderDesignation designation) {

	}

}
