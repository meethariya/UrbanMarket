/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.emailservice.dto;

import lombok.Data;

/**
 * Response DTO for product
 */
@Data
public class ResponseProductDto {
	private String id;
	private String name;
	private String brand;
	private double price;
}
