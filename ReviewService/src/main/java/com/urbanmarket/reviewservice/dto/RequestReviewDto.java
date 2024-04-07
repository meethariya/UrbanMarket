/**
 * 07-Apr-2024
 * meeth
 */
package com.urbanmarket.reviewservice.dto;

import java.util.Set;

import lombok.Data;

/**
 * Request dto for review
 */
@Data
public class RequestReviewDto {
	private String productId;
	private long customerId;
	private Set<String> imagePath;
	private byte rating;
	private String comment;
}
