/**
 * 18-May-2024
 * anike
 */
package com.urbanmarket.inventoryservice.exception;

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
