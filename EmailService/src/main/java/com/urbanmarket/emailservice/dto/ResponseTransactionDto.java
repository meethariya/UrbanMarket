/**
 * 28-Apr-2024
 * meeth
 */
package com.urbanmarket.emailservice.dto;

import java.util.Date;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

/**
 * Response dto for transaction.
 */
@Data
@Builder
public class ResponseTransactionDto {
	private UUID id;
	private String status;
	private Date timestamp;
	private Double amount;
}
