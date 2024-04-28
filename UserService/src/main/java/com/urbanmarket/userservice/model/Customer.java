/**
 * 09-Apr-2024
 * meeth
 */
package com.urbanmarket.userservice.model;

import java.util.Date;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Subclass of {@link User}. Made for additional customer details.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Customer extends User {

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dob;

	@Column(length = 10, unique = true, nullable = false)
	private String phone;

	@Column(nullable = false)
	private char gender;

	private String profilePicPath;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Map<String, Address> address;
}
