/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.productservice.dto;

import java.util.Set;

import com.urbanmarket.productservice.model.Category;

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
	private Set<String> imageUrl;
	private Category category;
}
