/**
 * 17-May-2024
 * meeth
 */
package com.urbanmarket.productservice.dto;

import java.util.Date;

import lombok.Data;

/**
 * Inventory along with product details
 */
@Data
public class ResponseFullInventory {
	private String id;
	private ResponseProductDto product;
	private long quantity;
	private Date importDate;
	private Date lastSoldDate;
}
