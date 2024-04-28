/**
 * 21-Apr-2024
 * meeth
 */
package com.urbanmarket.orderservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanmarket.orderservice.model.Order;

/**
 * Repository layer for order
 */
public interface OrderRepository extends JpaRepository<Order, UUID> {

	/**
	 * Find all orders placed by a customer
	 * 
	 * @param id customerId
	 * @return list of orders
	 */
	public List<Order> findByCustomerId(long id);
}
