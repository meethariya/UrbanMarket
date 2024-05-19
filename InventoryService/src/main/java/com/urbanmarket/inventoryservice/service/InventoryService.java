/**
 * 11-Apr-2024
 * anike
 */
package com.urbanmarket.inventoryservice.service;

import com.urbanmarket.inventoryservice.dto.RequestInventoryDto;
import com.urbanmarket.inventoryservice.dto.ResponseInventoryDto;
import com.urbanmarket.inventoryservice.exception.InventoryGenericException;
import com.urbanmarket.inventoryservice.exception.InventoryNotFoundException;
import com.urbanmarket.inventoryservice.model.Inventory;
import com.urbanmarket.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Service class for Inventory
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {

	private final InventoryRepository inventoryRepository;

	private final ModelMapper modelMapper;

	private static final String NOT_FOUND_MESSAGE="Inventory not found for given productId: ";

	/**
	 * Converts {@link Inventory} {@link ResponseInventoryDto}.
	 * 
	 * @param inventory
	 * @return
	 */
	public ResponseInventoryDto convertToResponseInventoryDto(Inventory inventory) {
		if (inventory == null) {
			throw new InventoryGenericException("IVNERROCC0002", "Inventory Not Found", null);
		}
		return modelMapper.map(inventory, ResponseInventoryDto.class);
	}

	/**
	 * Method to create {@link Inventory}.
	 * 
	 * @param inventoryDto
	 * @return
	 */
	public ResponseInventoryDto createProduct(RequestInventoryDto inventoryDto) {
		Inventory inventory = modelMapper.map(inventoryDto, Inventory.class);
		if(inventory.getImportDate()==null) {
			inventory.setImportDate(new Date());
		}
        log.info("Creating a new inventory: {}", inventory);
        return convertToResponseInventoryDto(inventoryRepository.save(inventory));
	}

	/**
	 * Method to get {@link Inventory}/ies.
	 * 
	 * @param productId
	 * @return
	 */
	public List<ResponseInventoryDto> getInventories(String productId) {
		log.info("Getting all the inventories");
		List<ResponseInventoryDto> inventoryList;
		if (productId != null) {
			Inventory inventory = inventoryRepository.findByProductId(productId);
			if (inventory == null) {
				throw new InventoryNotFoundException(NOT_FOUND_MESSAGE + productId);
			}
			inventoryList = Collections.singletonList(convertToResponseInventoryDto(inventory));
		} else {
			inventoryList = inventoryRepository.findAll().stream().map(this::convertToResponseInventoryDto).toList();
		}
		if (!inventoryList.isEmpty()) {
			return inventoryList;
		} else {
			throw new InventoryNotFoundException(NOT_FOUND_MESSAGE + productId);
		}
	}

	/**
	 * Method to update {@link Inventory}. Updates the fields that are changed.
	 * 
	 * @param inventoryDto
	 * @return
	 */
	public ResponseInventoryDto updateInventory(RequestInventoryDto inventoryDto) {
		log.debug("Updating Inventory");
		if (!inventoryRepository.existsByProductId(inventoryDto.getProductId())) {
			throw new InventoryNotFoundException(NOT_FOUND_MESSAGE + inventoryDto.getProductId());
		}
		Inventory savedInventory = inventoryRepository.findByProductId(inventoryDto.getProductId());
		if (savedInventory.equals(modelMapper.map(inventoryDto, Inventory.class))) {
			return convertToResponseInventoryDto(savedInventory);
		}
		if (!inventoryDto.getProductId().isEmpty() || !inventoryDto.getProductId().isBlank()) {
			savedInventory.setProductId(inventoryDto.getProductId());
		}
		if (inventoryDto.getQuantity() >= 0L) {
			savedInventory.setQuantity(inventoryDto.getQuantity());
		}
		if (inventoryDto.getImportDate() != null) {
			savedInventory.setImportDate(inventoryDto.getImportDate());
		}
		if (inventoryDto.getLastSoldDate() != null) {
			savedInventory.setLastSoldDate(inventoryDto.getLastSoldDate());
		}
        return convertToResponseInventoryDto(inventoryRepository.save(savedInventory));
	}

	/**
	 * Method to delete {@link Inventory}.
	 * 
	 * @param id
	 */
	public void deleteInventoryById(String id) {
		inventoryRepository.deleteById(id);
	}

	/**
	 * Method to delete inventory using productId
	 * 
	 * @param productId
	 */
	public void deleteByProductId(String productId) {
		log.info("INVENTORYSERVICE: Delete by productId");
		inventoryRepository.deleteByProductId(productId);
	}

	/**
	 * Get all inventory by product ids
	 * 
	 * @param id productIds
	 * @return List<responseInventoryDto>
	 */
	public List<ResponseInventoryDto> getByProductId(String[] id) {
		log.info("INVENTORYSERVICE: Get by inventories by productIds");
		
		List<ResponseInventoryDto> response = new ArrayList<>();
		for(String i : id) {
			Inventory inventory = inventoryRepository.findByProductId(i);
			if(inventory!=null) {
				response.add(modelMapper.map(inventory, ResponseInventoryDto.class));
			}
		}
		return response;
	}
}
