/**
 * 21-Apr-2024
 * meeth
 */
package com.urbanmarket.orderservice;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	/**
	 * Bean for model mapper. FUTURE: move to separate class
	 * 
	 * @return new object for model mapper.
	 */
	@Bean
	ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration()
		  .setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper;
	}
}
