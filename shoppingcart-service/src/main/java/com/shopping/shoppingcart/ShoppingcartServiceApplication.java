package com.shopping.shoppingcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableEurekaClient
public class ShoppingcartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingcartServiceApplication.class, args);
	}

//	@Bean("webClient")
//	@LoadBalanced
//	public WebClient.Builder getWebClint () {
//		return  WebClient.builder();
//	}

	@Bean("webClient")
	@LoadBalanced
	public WebClient.Builder getWebClient (){
		return  WebClient.builder();
	}
}
