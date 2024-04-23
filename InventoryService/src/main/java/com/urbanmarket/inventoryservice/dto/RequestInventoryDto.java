/**
 * 11-Apr-2024
 * anike
 */
package com.urbanmarket.inventoryservice.dto;

import java.util.Date;

import lombok.Data;

@Data
public class RequestInventoryDto {
	private String productId;
	private long quantity;
	private Date importDate;
	private Date lastSoldDate;
}
