/**
 * 21-Apr-2024
 * meeth
 */
package com.urbanmarket.orderservice.model;

import java.util.Date;
import java.util.Map;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model for cart list of user
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true, nullable = false)
	private long customerId;

	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "productId")
	@Column(name = "quantity")
	private Map<String, Integer> items;

	@Temporal(TemporalType.DATE)
	@UpdateTimestamp
	@Column(nullable = false)
	private Date modifiedOn;
}
