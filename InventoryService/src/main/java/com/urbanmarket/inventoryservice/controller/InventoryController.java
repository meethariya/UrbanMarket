/**
 * 10-Apr-2024
 * anike
 */
package com.urbanmarket.inventoryservice.controller;

/**
 * Controller layer for {@link Inventory}.
 */

import com.urbanmarket.inventoryservice.dto.RequestInventoryDto;
import com.urbanmarket.inventoryservice.dto.ResponseInventoryDto;
import com.urbanmarket.inventoryservice.model.UMResponse;
import com.urbanmarket.inventoryservice.service.InventoryService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/inventory")
public class InventoryController {

	private final InventoryService inventoryService;

	/**
	 * Creates a inventory<br>
	 * 
	 * @param inventoryDto
	 * @throws java.io.IOException
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<UMResponse> createProduct(@ModelAttribute RequestInventoryDto inventoryDto)
			throws IOException {
		ResponseInventoryDto responseInventoryDto = inventoryService.createProduct(inventoryDto);
		UMResponse umResponse = UMResponse.builder().data(responseInventoryDto)
				.status(UMResponse.ResponseStatus.SUCCESS).build();
		return ResponseEntity.ok(umResponse);
	}

	/**
	 * Get all inventories.
	 * 
	 * @return list of responseProductDto
	 */
	@GetMapping()
	public ResponseEntity<UMResponse> getInventories(@PathParam(value = "productId") String productId) {
		List<ResponseInventoryDto> responseInventoryDtos = inventoryService.getInventories(productId);
		UMResponse umResponse = UMResponse.builder().data(responseInventoryDtos)
				.status(UMResponse.ResponseStatus.SUCCESS).build();
		return ResponseEntity.ok(umResponse);
	}

	/**
	 * Update inventory details.<br>
	 * Throws
	 * {@link com.urbanmarket.inventoryservice.exception.InventoryNotFoundException}
	 * and
	 * {@link org.springframework.http.converter.HttpMessageNotReadableException}.
	 * 
	 * @param inventoryDto
	 * @return Updated {@link ResponseInventoryDto}
	 */
	@PutMapping()
	public ResponseEntity<UMResponse> updateInventory(@ModelAttribute RequestInventoryDto inventoryDto) {
		ResponseInventoryDto responseInventoryDto = inventoryService.updateInventory(inventoryDto);
		UMResponse umResponse = UMResponse.builder().data(responseInventoryDto)
				.status(UMResponse.ResponseStatus.SUCCESS).build();
		return ResponseEntity.ok(umResponse);
	}

	/**
	 * Delete inventory.
	 * 
	 * @param id Id.
	 */
	@DeleteMapping()
	public ResponseEntity<UMResponse> deleteInventoryById(@RequestParam String id) {
		inventoryService.deleteInventoryById(id);
		return ResponseEntity.ok(UMResponse.builder().data(null).status(UMResponse.ResponseStatus.SUCCESS).build());
	}

	/**
	 * Delete inventory by product id
	 * 
	 * @param productId
	 */
	@DeleteMapping("/product/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteByProductId(@PathVariable("id") String productId) {
		inventoryService.deleteByProductId(productId);
	}

	/**
	 * Get inventory by productId
	 * 
	 * @param productId id
	 * @return responseInventoryDto
	 */
	@GetMapping("/product/{id}")
	public ResponseEntity<ResponseInventoryDto> getInventoryByProductId(@PathVariable("id") String productId) {
		return new ResponseEntity<>(inventoryService.getByProductId(productId), HttpStatus.OK);
	}

}