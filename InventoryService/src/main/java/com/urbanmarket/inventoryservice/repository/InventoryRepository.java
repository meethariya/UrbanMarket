/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.inventoryservice.repository;

import com.urbanmarket.inventoryservice.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository layer for Inventory
 */
public interface InventoryRepository extends MongoRepository<Inventory, String> {

    boolean existsByProductId(String productId);

    Inventory findByProductId(String productId);
}
