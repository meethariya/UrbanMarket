/**
 * 09-Apr-2024
 * meeth
 */
package com.urbanmarket.userservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.urbanmarket.userservice.dto.RequestCustomerDto;
import com.urbanmarket.userservice.dto.ResponseCustomerDto;
import com.urbanmarket.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Controller layer for user
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
	 * Create customer.
	 * 
	 * @param customerDto requestCustomerDto
	 * @throws IOException if image is not saved correctly
	 * @return customerId
	 */
	@PostMapping("/create")
	public ResponseEntity<Long> createCustomer(@RequestBody RequestCustomerDto customerDto) {
		return new ResponseEntity<>(userService.createCustomer(customerDto), HttpStatus.CREATED);
	}

	/**
	 * Get all customers
	 * 
	 * @return list of responseCustomerDto
	 */
	@GetMapping
	public ResponseEntity<List<ResponseCustomerDto>> getAllCustomers() {
		return new ResponseEntity<>(userService.getAllCustomer(), HttpStatus.OK);
	}

	/**
	 * Get customer by id
	 * 
	 * @param id customerId
	 * @return responseCustomerDto
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ResponseCustomerDto> getCustomerById(@PathVariable("id") long id) {
		return new ResponseEntity<>(userService.getCustomerById(id), HttpStatus.OK);
	}

	/**
	 * Update customer.
	 * 
	 * @param id          customerId
	 * @param customerDto requestCustomerDto
	 * @return updated responseCustomerDto
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ResponseCustomerDto> updateCustomer(@PathVariable("id") long id,
			@RequestBody RequestCustomerDto customerDto) {
		return new ResponseEntity<>(userService.editCustomer(id, customerDto), HttpStatus.ACCEPTED);
	}

	/**
	 * Delete customer.
	 * 
	 * @param id customerId
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteCustomer(@PathVariable("id") long id) {
		userService.deleteCustomer(id);
	}

	/**
	 * Add/update profile picture for a customer
	 * 
	 * @param id   customer id
	 * @param file profile picture
	 * @throws IOException if image is not saved properly
	 */
	@PostMapping("/profilePic/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void addProfilePicture(@PathVariable("id") long id,
			@RequestParam(name = "image", required = false) MultipartFile file) throws IOException {
		userService.addProfilePicture(id, file);
	}

}
