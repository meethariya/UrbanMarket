/**
 * 13-Apr-2024
 * meeth
 */
package com.urbanmarket.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.urbanmarket.userservice.dto.RequestAddressDto;
import com.urbanmarket.userservice.dto.ResponseAddressDto;
import com.urbanmarket.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Controller layer for customer's address
 */
@RestController
@RequestMapping("/api/user/address")
@RequiredArgsConstructor
public class AddressController {

	private final UserService userService;

	/**
	 * Add new address for a customer.
	 * 
	 * @param id         customerId
	 * @param addressDto requestAddressDto
	 */
	@PostMapping("/customerId/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public void createAddress(@PathVariable("id") long id, @RequestBody RequestAddressDto addressDto) {
		userService.createAddress(id, addressDto);
	}

	/**
	 * Updates address.
	 * 
	 * @param addressId  address Id
	 * @param addressDto new address details
	 * @return updated responseAddressDto
	 */
	@PutMapping("/{addressId}")
	public ResponseEntity<ResponseAddressDto> updateAddress(@PathVariable("addressId") long addressId,
			@RequestBody RequestAddressDto addressDto) {
		return new ResponseEntity<>(userService.updateAddress(addressId, addressDto), HttpStatus.ACCEPTED);
	}

	/**
	 * Delete an address by customer id and it's name
	 * 
	 * @param id  customer's id
	 * @param key address key
	 */
	@DeleteMapping("/{customerId}/{key}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteAddress(@PathVariable("customerId") long id, @PathVariable("key") String key) {
		userService.deleteAddress(id, key);
	}

}
