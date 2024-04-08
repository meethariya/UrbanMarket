/**
 * 08-Apr-2024
 * meeth
 */
package com.urbanmarket.reviewservice.dto;

import lombok.Data;

/**
 * DTO to edit review model
 */
@Data
public class EditReviewDto {
	private byte rating;
	private String comment;
}
