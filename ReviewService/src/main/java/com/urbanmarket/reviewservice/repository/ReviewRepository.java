/**
 * 07-Apr-2024
 * meeth
 */
package com.urbanmarket.reviewservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.urbanmarket.reviewservice.model.Review;

/**
 * Repository Layer for review
 */
public interface ReviewRepository extends MongoRepository<Review, String> {

	/**
	 * Find Review for a specific product by a given user.
	 * 
	 * @param productId  product's id
	 * @param customerId customer's id
	 * @return optional of review model.
	 */
	public Optional<Review> findByProductIdAndCustomerId(String productId, long customerId);

	/**
	 * Find all reviews of a product.
	 * 
	 * @param productId id
	 * @return list of product's reviews
	 */
	public List<Review> findByProductId(String productId);

	/**
	 * Find all reviews by a customer.
	 * 
	 * @param customerId id
	 * @return list of reviews
	 */
	public List<Review> findByCustomerId(long customerId);
}
