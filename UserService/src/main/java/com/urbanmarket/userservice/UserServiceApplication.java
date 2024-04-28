package com.urbanmarket.userservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	/**
	 * Bean for model mapper. FUTURE: move to separate class
	 * 
	 * @return new object for model mapper.
	 */
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
