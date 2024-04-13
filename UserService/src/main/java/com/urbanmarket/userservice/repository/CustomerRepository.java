/**
 * 09-Apr-2024
 * meeth
 */
package com.urbanmarket.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanmarket.userservice.model.Customer;

/**
 * Repository layer for {@link Customer}
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	/**
	 * Find customer by email and phone number.
	 * 
	 * @param email customer's unique email
	 * @param phone customer's unique phone
	 * @return Optional of customer
	 */
	public Optional<Customer> findByEmailAndPhone(String email, String phone);
}
