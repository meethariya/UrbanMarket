/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.inventoryservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.urbanmarket.inventoryservice.model.Inventory;

/**
 * Repository layer for Inventory
 */
public interface InventoryRepository extends MongoRepository<Inventory, String> {

}
