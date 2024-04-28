/**
 * 21-Apr-2024
 * meeth
 */
package com.urbanmarket.orderservice.dto;

import java.util.Map;

import lombok.Data;

/**
 * Request dto for cart.
 */
@Data
public class RequestCartDto {
	private long customerId;
	private Map<String, Integer> items;
}
