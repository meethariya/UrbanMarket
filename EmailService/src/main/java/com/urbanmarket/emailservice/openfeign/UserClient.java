/**
 * 28-Apr-2024
 * meeth
 */
package com.urbanmarket.emailservice.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.urbanmarket.emailservice.dto.ResponseCustomerDto;

/**
 * Feign client for order service
 */
@FeignClient(name = "userservice", configuration = FeignClientConfig.class)
public interface UserClient {
	/**
	 * Get customer by id
	 * 
	 * @param id customerId
	 * @return responseCustomerDto
	 */
	@GetMapping("/api/user/{id}")
	public ResponseCustomerDto getCustomerById(@PathVariable("id") long id);
}
