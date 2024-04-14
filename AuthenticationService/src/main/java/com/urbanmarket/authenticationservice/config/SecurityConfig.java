/**
 * 14-Apr-2024
 * meeth
 */
package com.urbanmarket.authenticationservice.config;

import static org.springframework.security.config.Customizer.withDefaults;

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

/**
 * Spring Security configuration for filterchain and AuthenticationProvider.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
				requests -> requests.requestMatchers(new AntPathRequestMatcher("/api/authentication/generateToken"))
						.authenticated().requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
				.csrf(c -> c.disable()).formLogin(withDefaults()).oauth2Login(withDefaults());
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
}
