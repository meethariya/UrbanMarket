/**
 * 14-May-2024
 * meeth
 */
package com.urbanmarket.productservice.openfeign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.urbanmarket.productservice.dto.RequestInventoryDto;
import com.urbanmarket.productservice.dto.ResponseInventoryDto;

/**
 * Feign client for inventory service
 */
@FeignClient(name = "inventoryservice", configuration = FeignClientConfig.class)
public interface InventoryClient {

	/**
	 * Create inventory object.
	 * 
	 * @param inventoryDto
	 */
	@PostMapping(value = "/api/inventory", consumes = "multipart/form-data")
	public void createProduct(RequestInventoryDto inventoryDto);

	/**
	 * Delete inventory by product id
	 * 
	 * @param productId
	 */
	@DeleteMapping("/api/inventory/product/{id}")
	public void deleteByProductId(@PathVariable("id") String productId);

	/**
	 * Get all inventory by productId
	 * 
	 * @param productId
	 * @return ResponseInventoryDto
	 */
	@GetMapping("/api/inventory/multiple-product")
	public List<ResponseInventoryDto> getInventoryByProductId(@RequestParam("ids") String[] productIds);
}
