/**
 * 18-May-2024
 * meeth
 */
package com.urbanmarket.productservice.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for review service
 */
@FeignClient(name = "reviewservice", configuration = FeignClientConfig.class)
public interface ReviewClient {
	/**
	 * Delete all reviews of a product
	 * 
	 * @param id productId
	 */
	@DeleteMapping("/api/review/product/{id}")
	public void deleteReviewOfProduct(@PathVariable("id") String id);
}
