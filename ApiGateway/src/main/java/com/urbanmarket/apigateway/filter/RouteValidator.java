/**
 * 13-May-2024
 * meeth
 */
package com.urbanmarket.apigateway.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * This class is used to filter request on how they will be authenticated
 */
public final class RouteValidator {

	/**
	 * Private constructor to hide public one
	 */
	private RouteValidator() {}
	
	/**
	 * List of APIs that can bypass filter
	 */
	private static final List<String> permitAll = List.of("/api/authentication/**");

	/**
	 * List of APIs that are specifically for admin only.
	 * TODO: Segregate admin and customer APIs from each service and add APIs here
	 */
	private static final List<String> admin = List.of();

	/**
	 * Predicate to match request with list of permitted url.
	 */
	public static final Predicate<ServerHttpRequest> isSecured = req -> permitAll.stream()
			.noneMatch(uri -> req.getURI().getPath().contains(uri));

	/**
	 * Predicate to match request with list of url only for admins
	 */
	public static final Predicate<ServerHttpRequest> isAdmin = req -> admin.stream()
			.anyMatch(uri -> req.getURI().getPath().contains(uri));
}
