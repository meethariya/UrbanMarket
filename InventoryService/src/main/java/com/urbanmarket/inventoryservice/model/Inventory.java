/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.inventoryservice.model;

import com.mongodb.lang.NonNull;
import lombok.*;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

/**
 * Model class for {@link Inventory}.
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {
	@MongoId
	private String id;
	@Indexed(unique = true)
	@NonNull
	private String productId;
	private long quantity;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date importDate;
	private Date lastSoldDate;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Inventory inventory = (Inventory) o;
		return quantity == inventory.quantity && Objects.equals(productId, inventory.productId) && Objects.equals(importDate, inventory.importDate) && Objects.equals(lastSoldDate, inventory.lastSoldDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, quantity, importDate, lastSoldDate);
	}
}
