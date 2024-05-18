/**
 * 18-May-2024
 * meeth
 */
package com.urbanmarket.userservice.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for review service
 */
@FeignClient(name = "reviewservice", configuration = FeignClientConfig.class)
public interface ReviewClient {
	/**
	 * Delete all reviews of a customer
	 * 
	 * @param id customerId
	 */
	@DeleteMapping("/api/review/customer/{id}")
	public void deleteReviewOfCustomer(@PathVariable("id") long id);
}
