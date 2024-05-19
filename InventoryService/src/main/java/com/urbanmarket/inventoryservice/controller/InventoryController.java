/**
 * 10-Apr-2024
 * anike
 */
package com.urbanmarket.inventoryservice.controller;

import com.urbanmarket.inventoryservice.dto.RequestInventoryDto;
import com.urbanmarket.inventoryservice.dto.ResponseInventoryDto;
import com.urbanmarket.inventoryservice.service.InventoryService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<ResponseInventoryDto> createProduct(@ModelAttribute RequestInventoryDto inventoryDto) {
		ResponseInventoryDto responseInventoryDto = inventoryService.createProduct(inventoryDto);
		return ResponseEntity.ok(responseInventoryDto);
	}

	/**
	 * Get all inventories.
	 * 
	 * @return list of responseProductDto
	 */
	@GetMapping()
	public ResponseEntity<List<ResponseInventoryDto>> getInventories(@PathParam(value = "productId") String productId) {
		List<ResponseInventoryDto> responseInventoryDtos = inventoryService.getInventories(productId);
		return ResponseEntity.ok(responseInventoryDtos);
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
	public ResponseEntity<ResponseInventoryDto> updateInventory(@ModelAttribute RequestInventoryDto inventoryDto) {
		ResponseInventoryDto responseInventoryDto = inventoryService.updateInventory(inventoryDto);
		return ResponseEntity.ok(responseInventoryDto);
	}

	/**
	 * Delete inventory.
	 * 
	 * @param id Id.
	 */
	@DeleteMapping()
	@ResponseStatus(HttpStatus.OK)
	public void deleteInventoryById(@RequestParam String id) {
		inventoryService.deleteInventoryById(id);
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
	 * Get inventories by productIds
	 * 
	 * @param productIds id
	 * @return List<responseInventoryDto>
	 */
	@GetMapping("/multiple-product")
	public ResponseEntity<List<ResponseInventoryDto>> getInventoryByProductId(@RequestParam("ids") String[] productIds) {
		return new ResponseEntity<>(inventoryService.getByProductId(productIds), HttpStatus.OK);
	}

}