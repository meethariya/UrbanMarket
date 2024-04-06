/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.productservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.urbanmarket.productservice.model.Product;

/**
 * Repository Layer for product
 */
public interface ProductRepository extends MongoRepository<Product, String> {

	/**
	 * Find product by name and brand.
	 * @param name productName
	 * @param brand productBrand
	 * @return Optional of product
	 */
	public Optional<Product> findByNameAndBrand(String name, String brand);
}
