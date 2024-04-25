/**
 * 21-Apr-2024
 * meeth
 */
package com.urbanmarket.orderservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanmarket.orderservice.model.Cart;

/**
 * Repository layer for cart
 */
public interface CartRepositoy extends JpaRepository<Cart, Long> {

	/**
	 * Get cart of a customer
	 * 
	 * @param id customer id
	 * @return optional of cart
	 */
	public Optional<Cart> findByCustomerId(long id);

	/**
	 * Delete cart of a customer
	 * 
	 * @param id customer id
	 */
	public void deleteByCustomerId(long id);
}
