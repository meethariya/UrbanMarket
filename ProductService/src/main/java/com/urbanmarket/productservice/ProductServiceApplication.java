package com.urbanmarket.productservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	/**
	 * Additional bean for modelMapper.<br>
	 * <b>Future: keep in separate file</b>
	 * @return object for ModelMapper.
	 */
    @Bean
    ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
