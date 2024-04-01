/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.urbanmarket.productservice.model.Product;

/**
 * Repository Layer for product
 */
public interface ProductRepository extends MongoRepository<Product, String> {

}
