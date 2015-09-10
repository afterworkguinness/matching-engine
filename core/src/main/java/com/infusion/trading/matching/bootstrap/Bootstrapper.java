package com.infusion.trading.matching.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.infusion.trading.matching.matcher.OrderFillService;

public class Bootstrapper {
	
	private static Logger LOGGER = LoggerFactory.getLogger(Bootstrapper.class);
	private static OrderFillService INSTANCE=null;
	/*
	 * Why this ugliness ?
	 * 
	 * <rant> Getting Spring to work with Jersey in the web tier is proving to
	 * be a pain in the ass. After 2 days of fiddling with it and trying many
	 * tutorials I'm giving up at this point and will get it working later.
	 * </rant>
	 * 
	 * In the mean time, to get a Spring managed singleton of OrderFillService
	 * in the web tier, we can bootstrap it this way. Ugly, but it works!
	 */

	public static OrderFillService getOrderFillService() {
		
		if(INSTANCE == null) {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("matching-engine-spring-context-root.xml");
		
		INSTANCE = context.getBean(OrderFillService.class);
		}
		return INSTANCE;
	}
}
