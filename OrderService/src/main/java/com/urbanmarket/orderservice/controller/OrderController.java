/**
 * 21-Apr-2024
 * meeth
 */
package com.urbanmarket.orderservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanmarket.orderservice.dto.RequestCartDto;
import com.urbanmarket.orderservice.dto.RequestOrderDto;
import com.urbanmarket.orderservice.dto.ResponseCartDto;
import com.urbanmarket.orderservice.dto.ResponseOrderDto;
import com.urbanmarket.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

/**
 * Controller layer for order and cart.
 */
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	/**
	 * Create cart of a customer
	 * 
	 * @param cartDto requestCartDto
	 * @return created responseCartDto
	 */
	@PostMapping("/cart")
	public ResponseEntity<ResponseCartDto> createCart(@RequestBody RequestCartDto cartDto) {
		return new ResponseEntity<>(orderService.createUpdateCart(cartDto), HttpStatus.CREATED);
	}

	/**
	 * Update cart of a customer
	 * 
	 * @param cartDto requestCartDto
	 * @return updated responseCartDto
	 */
	@PutMapping("/cart")
	public ResponseEntity<ResponseCartDto> updateCart(@RequestBody RequestCartDto cartDto) {
		return new ResponseEntity<>(orderService.createUpdateCart(cartDto), HttpStatus.ACCEPTED);
	}

	/**
	 * Get cart of a customer
	 * 
	 * @param id customerId
	 * @return responseCartDto
	 */
	@GetMapping("/cart/{id}")
	public ResponseEntity<ResponseCartDto> getCart(@PathVariable("id") long id) {
		return new ResponseEntity<>(orderService.getCustomerCart(id), HttpStatus.OK);
	}

	/**
	 * Delete cart of a customer
	 * 
	 * @param id customerId
	 */
	@DeleteMapping("/cart/{id}")
	public void deleteCart(@PathVariable("id") long id) {
		orderService.deleteCart(id);
	}

	/**
	 * Place an order
	 * 
	 * @param orderDto requestOrderDto
	 * @return created responseOrderDto
	 */
	@PostMapping
	public ResponseEntity<ResponseOrderDto> createOrder(@RequestBody RequestOrderDto orderDto) {
		return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
	}

	/**
	 * Get all orders
	 * 
	 * @return list of responseOrderDto
	 */
	@GetMapping
	public ResponseEntity<List<ResponseOrderDto>> getOrders() {
		return new ResponseEntity<>(orderService.getOrder(), HttpStatus.OK);
	}

	/**
	 * Get all orders of customer
	 * 
	 * @param id customerId
	 * @return list of responseOrderDto
	 */
	@GetMapping("/customer/{id}")
	public ResponseEntity<List<ResponseOrderDto>> getOrdersOfCustomer(@PathVariable("id") long id) {
		return new ResponseEntity<>(orderService.getOrderOfCustomer(id), HttpStatus.OK);
	}

	/**
	 * Get order by id
	 * 
	 * @param id uuid
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ResponseOrderDto> getOrdersById(@PathVariable("id") UUID id) {
		return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
	}
}
