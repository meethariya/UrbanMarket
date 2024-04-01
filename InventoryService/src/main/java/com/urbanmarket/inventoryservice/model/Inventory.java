/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.inventoryservice.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {
	@MongoId
	private String id;
	private String productId;
	private long quantity;
	private Date importDate;
	private Date lastSoldDate;
}
