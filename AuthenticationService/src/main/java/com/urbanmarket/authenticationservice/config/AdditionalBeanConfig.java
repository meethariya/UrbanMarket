/**
 * 14-Apr-2024
 * meeth
 */
package com.urbanmarket.authenticationservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration file for all additional beans
 */
@Configuration
public class AdditionalBeanConfig {

	/**
	 * Bean for {@link ModelMapper}.
	 * 
	 * @return new object for ModelMapper
	 */
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	/**
	 * Bean for {@link BCryptPasswordEncoder}.
	 * 
	 * @return new object for PasswordEncoder
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Bean for {@link AuthenticationManager}.
	 * 
	 * @param config authentication configuration
	 * @return object for AuthenticationManager
	 * @throws Exception
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
