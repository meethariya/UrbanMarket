/**
 * 21-Apr-2024
 * meeth
 */
package com.urbanmarket.orderservice.dto;

import java.util.Date;
import java.util.Map;

import lombok.Data;

/**
 * Response dto for cart
 */
@Data
public class ResponseCartDto {

	private long id;

	private long customerId;

	private Map<String, Integer> items;

	private Date modifiedOn;
}
