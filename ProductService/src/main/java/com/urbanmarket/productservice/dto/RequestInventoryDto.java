/**
 * 11-Apr-2024
 * anike
 */
package com.urbanmarket.productservice.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestInventoryDto {
	private String productId;
	private long quantity;
	private Date importDate;
	private Date lastSoldDate;
}
