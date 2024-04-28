/**
 * 21-Apr-2024
 * meeth
 */
package com.urbanmarket.orderservice.dto;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import lombok.Data;

/**
 * Response dto for order.
 */
@Data
public class ResponseOrderDto {
	private UUID id;
	private long customerId;
	private Map<String, Integer> items;
	private Date placedOn;
	private UUID transactionId;
}
