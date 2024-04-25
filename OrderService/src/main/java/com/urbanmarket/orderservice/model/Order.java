/**
 * 21-Apr-2024
 * meeth
 */
package com.urbanmarket.orderservice.model;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.SourceType;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model for customer's orders
 */
@Entity
@Table(name = "orderTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, updatable = false)
	private long customerId;

	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "productId")
	@Column(name = "quantity")
	private Map<String, Integer> items;

	@Temporal(TemporalType.TIMESTAMP)
	@CurrentTimestamp(source = SourceType.VM)
	@Column(nullable = false, updatable = false)
	private Date placedOn;

	@Column(unique = true, nullable = false, updatable = false)
	private UUID transactionId;

}
