/**
 * 09-Apr-2024
 * meeth
 */
package com.urbanmarket.userservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Address model for customers.
 * 
 * @see Customer
 */
@Data
@Entity
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 30, nullable = false)
	private String houseNo;
	
	@Column(length = 50, nullable = false)
	private String addressLine1;
	
	@Column(length = 50)
	private String addressLine2;
	
	@Column(length = 30, nullable = false)
	private String city;
	
	@Column(length = 30, nullable = false)
	private String state;
	
	@Column(nullable = false)
	private int pincode;
}
