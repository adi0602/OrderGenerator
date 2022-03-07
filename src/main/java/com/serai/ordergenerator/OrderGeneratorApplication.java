package com.serai.ordergenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import com.serai.ordergenerator.service.OrderGeneratorService;

@SpringBootApplication
public class OrderGeneratorApplication implements CommandLineRunner {

	
	private static final Logger log = LoggerFactory.getLogger(OrderGeneratorApplication.class);

	@Autowired
	private OrderGeneratorService generatorService;
		
	public static void main(String[] args) {
		log.info("STARTING ORDER GENERATOR");
		SpringApplication.run(OrderGeneratorApplication.class, args);
		log.info("ORDER GENERATOR CLOSED");
	}


	@Override
	public void run(String... args) throws Exception {
		
		if(args[0].length() > 0) {
			try {
				int n = Integer.parseInt(args[0]);			
				if(n < 0) {
					log.error("Please provide a valid positive number of orders to be generated as argument");
				}
				else {
					log.info("Preparing to send " + n + " orders");
					
					generatorService.generateAndSendOrders(n);
				}
			}
			catch(NumberFormatException e) {
				log.error("Please provide a valid positive number of orders to be generated as argument");
			}
		}
		else {
			log.error("Please provide a valid positive number of orders to be generated as argument");
		}
	}
}
