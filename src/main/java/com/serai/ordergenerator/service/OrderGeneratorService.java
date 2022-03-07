package com.serai.ordergenerator.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.serai.ordergenerator.model.Order;

@Service
public class OrderGeneratorService {
	
	@Value("${pizza-joint.username}")
	private String username;
	
	@Value("${pizza-joint.password}")
	private String password;
	
	private final PizzaJointService pizzaJointService;
	
	private static final Logger log = LoggerFactory.getLogger(OrderGeneratorService.class);
	
	public OrderGeneratorService(PizzaJointService pizzaJointService) {
		this.pizzaJointService = pizzaJointService;
	}

	/**
	 * Generates random name, quantity & price to send order in post request 
	 */
	public void generateAndSendOrders(int n) {
		
    	if(pizzaJointService.authenticate(username, password)) {

    		for(int i = 0; i < n; i++) {
	    		
	    		int leftLimit = 97; 
	    	    int rightLimit = 122;
	    	    int targetStringLength = 10;
	    	    Random random = new Random();

	    	    String name = random.ints(leftLimit, rightLimit + 1)
	        	      .limit(targetStringLength)
	        	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	        	      .toString();

	    	    int quantity = random.ints(1, 1, 100).findFirst().getAsInt();
	    	    int price = random.ints(1, 100, 1000).findFirst().getAsInt();
	    	    
	    	    Order order = new Order(name, quantity, price);
	    	    log.debug("Generated: " + order);
	    		
	    	    try {
	    	    	log.info("Sending request to save new " + order);
	    	    	order = pizzaJointService.saveOrder(order);
	    	    	if(order != null)
	    	    		log.info("Order saved successfully");
	    	    }
	    	    catch(Exception e) {
	    	    	log.error(e.getMessage().toString());
	    	    }
	    	}
    	}
	}
}
