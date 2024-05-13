/**
 * 13-May-2024
 * meeth
 */
package com.urbanmarket.apigateway.filter;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.urbanmarket.apigateway.exception.UnauthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Filter all requests according to {@link RouteValidator}.
 */
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	/**
	 * secret for JWT token validation.
	 */
	@Value("${JWT.secret}")
	private String secret;

	/**
	 * Static class required for {@link AbstractGatewayFilterFactory}.
	 */
	public static class Config {
	}

	/**
	 * Pass {@link Config} to parent on initialize
	 */
	public AuthenticationFilter() {
		super(Config.class);
	}

	/**
	 * Filter all requests if its secured. Validate the JWT and also validate if the
	 * request is for admin or customer and reroute accordingly.
	 * 
	 * @param config
	 */
	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {

			// validation for secured url
			if (RouteValidator.isSecured.test(exchange.getRequest())) {
				// check if token exists in headers or not
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new UnauthorizedException("Missing Authorization headers");
				}

				// check if header has token or other authorization
				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					authHeader = authHeader.substring(7);
				}

				// validate token and get all claims
				validateToken(authHeader);
				Claims claims = getAllClaims(authHeader);

				// get role here
				String role = claims.get("role", String.class);

				// validate if the url is for admin only, if so check if token is of admin
				if (RouteValidator.isAdmin.test(exchange.getRequest()) && !role.equals("admin")) {
					throw new UnauthorizedException("Unauthorized");
				}
			}

			return chain.filter(exchange);
		});

	}

	/**
	 * Validate JWT token
	 * 
	 * @param token JWT
	 */
	private void validateToken(String token) {
		Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
	}

	/**
	 * Get Key
	 * 
	 * @return Key for JWT using secret
	 */
	private Key getSignKey() {
		byte[] signKey = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(signKey);
	}

	/**
	 * Get all claims of a token.
	 * 
	 * @param token
	 * @return Map of claims
	 */
	private Claims getAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}
}
