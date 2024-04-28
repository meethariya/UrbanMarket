/**
 * 11-Apr-2024
 * meeth
 */
package com.urbanmarket.userservice.dto;

import java.util.Date;
import java.util.Map;

import com.urbanmarket.userservice.model.Address;
import com.urbanmarket.userservice.model.Role;

import lombok.Data;

/**
 * Request dto for customer model
 */
@Data
public class RequestCustomerDto {
	private String name;

	private String email;

	private Role role;

	private Date dob;

	private String phone;

	private char gender;

	private String profilePicPath;

	private Map<String, Address> address;
}
