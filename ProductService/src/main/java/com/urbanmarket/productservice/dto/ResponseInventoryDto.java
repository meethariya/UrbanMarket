/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.productservice.dto;

import java.util.Date;

import lombok.Data;
@Data
public class ResponseInventoryDto {
	private String id;
	private String productId;
	private long quantity;
	private Date importDate;
	private Date lastSoldDate;
}
