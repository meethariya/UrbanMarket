/**
 * 14-Apr-2024
 * meeth
 */
package com.urbanmarket.authenticationservice.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * Spring Security configuration for filter chain and AuthenticationProvider.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${angular}")
	private String angular;
	
	private static final String SUCCESSURL = "/api/authentication/generateToken";
	/**
	 * Filter chain for all requests for authentication service.
	 * 
	 * @param http requests
	 * @return filterchain
	 * @throws Exception
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				requests -> requests.requestMatchers(new AntPathRequestMatcher(SUCCESSURL))
						.authenticated().requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
				.csrf(c -> c.disable()).cors(withDefaults())
				.formLogin(s -> s.successForwardUrl(SUCCESSURL).permitAll())
				.oauth2Login(s -> s.defaultSuccessUrl(SUCCESSURL).permitAll());
		return http.build();
	}

	/**
	 * Validate user credentials from database.
	 * 
	 * @param userDetailsService service Layer to validate userDetails
	 * @param passwordEncoder    BCrypt
	 * @return AuthenticationProvider
	 */
	@Bean
	AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
		authenticationProvider.setUserDetailsService(userDetailsService);
		return authenticationProvider;
	}

	@Bean
	CorsWebFilter corsWebFilter() {

		final CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(Collections.singletonList(angular));
		corsConfig.setMaxAge(3600L);
		corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		corsConfig.addAllowedHeader("*");

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);

		return new CorsWebFilter(source);
	}
}
