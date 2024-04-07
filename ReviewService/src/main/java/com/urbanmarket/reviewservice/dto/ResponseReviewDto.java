/**
 * 07-Apr-2024
 * meeth
 */
package com.urbanmarket.reviewservice.dto;

import java.util.Date;
import java.util.Set;

import lombok.Data;

/**
 * Response dto for review
 */
@Data
public class ResponseReviewDto {
	private String id;
	private String productId;
	private long customerId;
	private Set<String> imagePath;
	private byte rating;
	private String comment;
	private Date modifiedOn;
}
