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
    /**
     * Method to check is the {@link Inventory} exists by productId
     * @param productId
     * @return
     */
    boolean existsByProductId(String productId);

    /**
     * Method to find {@link Inventory} by productId.
     * @param productId
     * @return
     */
    Inventory findByProductId(String productId);
}
