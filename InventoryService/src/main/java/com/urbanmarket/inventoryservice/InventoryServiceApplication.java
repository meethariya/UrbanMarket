/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.inventoryservice;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	/**
	 * Additional bean for modelMapper.<br>
	 * <b>Future: keep in separate file</b>
	 * @return object for ModelMapper.
	 */
    @Bean
    public ModelMapper modelMapper() {
    	ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}
}
