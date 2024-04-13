/**
 * 12-Apr-2024
 * meeth
 */
package com.urbanmarket.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanmarket.userservice.model.Address;

/**
 * Repository layer for address model
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

}
