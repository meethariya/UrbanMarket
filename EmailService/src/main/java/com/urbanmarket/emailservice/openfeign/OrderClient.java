/**
 * 28-Apr-2024
 * meeth
 */
package com.urbanmarket.emailservice.openfeign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.urbanmarket.emailservice.dto.ResponseOrderDto;

/**
 * Feign client for order service
 */
@FeignClient(name = "orderservice", configuration = FeignClientConfig.class)
public interface OrderClient {
	/**
	 * Get order by id
	 * 
	 * @param id uuid
	 * @return
	 */
	@GetMapping("/api/order/{id}")
	public ResponseOrderDto getOrdersById(@PathVariable("id") UUID id);
}
