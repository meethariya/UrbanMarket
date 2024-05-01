/**
 * 21-Apr-2024
 * meeth
 */
package com.urbanmarket.orderservice.dto;

import java.util.Map;
import java.util.UUID;

import lombok.Data;

/**
 * Request dto for order
 */
@Data
public class RequestOrderDto {
	private long customerId;
	private String addressType;
	private Map<String, Integer> items;
	private UUID transactionId;
}
