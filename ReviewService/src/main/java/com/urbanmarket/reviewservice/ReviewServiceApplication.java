/**
 * 07-Apr-2024
 * meeth
 */
package com.urbanmarket.reviewservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ReviewServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewServiceApplication.class, args);
	}

	/**
	 * Bean for model mapper.<br>
	 * Future: add to another file.
	 * 
	 * @return new object for ModelMapper.
	 */
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
