/**
 * 12-Apr-2024
 * meeth
 */
package com.urbanmarket.userservice.dto;

import lombok.Data;

/**
 * ResponseDto for address
 */
@Data
public class ResponseAddressDto {
	private long id;

	private String houseNo;

	private String addressLine1;

	private String addressLine2;

	private String city;

	private String state;

	private int pincode;
}
