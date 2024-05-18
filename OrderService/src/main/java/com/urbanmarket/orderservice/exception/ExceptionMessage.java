/**
 * 18-May-2024
 * meeth
 */
package com.urbanmarket.orderservice.exception;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * Standard format to throw exceptions
 */
@Data
@Builder
public class ExceptionMessage {
	private Date timestamp;
    private String status;
    private String title;
    private String message;
}
