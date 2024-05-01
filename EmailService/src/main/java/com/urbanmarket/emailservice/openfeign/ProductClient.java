/**
 * 28-Apr-2024
 * meeth
 */
package com.urbanmarket.emailservice.openfeign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.urbanmarket.emailservice.dto.ResponseProductDto;

/**
 * Feign client for product service
 */
@FeignClient(name = "productservice", configuration = FeignClientConfig.class)
public interface ProductClient {

	/**
	 * Get multiple products using respective ids.
	 *
	 * @param id list of productIds
	 * @return list of responseProductDto
	 */
	@GetMapping("/api/product/multiple-products")
	public List<ResponseProductDto> getMultipleProductsById(@RequestParam("id") String[] id);
}
