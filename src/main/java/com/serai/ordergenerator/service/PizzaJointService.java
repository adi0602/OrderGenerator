package com.serai.ordergenerator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serai.ordergenerator.model.Order;
import com.serai.ordergenerator.model.ResponseToken;
import com.serai.ordergenerator.model.User;

@Service
public class PizzaJointService {
	
	private static final Logger log = LoggerFactory.getLogger(PizzaJointService.class);
	
	@Value("${pizza-joint.url}")
	private String url;
	
	private String token;
	
	public PizzaJointService() {
	}
	
	@Bean
	private RestTemplate restTemplate() {
		return new RestTemplate();
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}
	
	public boolean authenticate(String username, String password) {
		try {
			log.info("Attempting Authentication");
			User authenticationUser = new User(username, password);
			String authenticationBody = new ObjectMapper().writeValueAsString(authenticationUser);
			HttpHeaders authenticationHeaders = getHeaders();
			HttpEntity<String> authenticationEntity = new HttpEntity<String>(authenticationBody,
					authenticationHeaders);

			ResponseEntity<ResponseToken> authenticationResponse = restTemplate().exchange(url + "/authenticate",
					HttpMethod.POST, authenticationEntity, ResponseToken.class);
				
			if (authenticationResponse != null && authenticationResponse.getStatusCode().equals(HttpStatus.OK)) {
				log.info("Authentication Successful");
				this.token = "Bearer " + authenticationResponse.getBody().getToken();
				return true;
			}
		} catch (Exception ex) {
			log.info("Authentication Failed");
			log.error(ex.toString());
			System.out.println(ex);
		}
		log.info("Authentication Failed");
		return false;
	}

	public Order saveOrder(Order order) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		HttpEntity<Order> request = new HttpEntity<>(order, headers);
		ResponseEntity<Order> response =  restTemplate().postForEntity(url + "/order", request, Order.class);
		
		if(response.getStatusCode() == HttpStatus.CREATED) {
			return response.getBody();
		}
		else {
			throw new Exception(response.getBody().toString());
		}
	}
}
