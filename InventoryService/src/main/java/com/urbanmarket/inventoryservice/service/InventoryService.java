/**
 * 11-Apr-2024
 * anike
 */
package com.urbanmarket.inventoryservice.service;

import com.urbanmarket.inventoryservice.dto.RequestInventoryDto;
import com.urbanmarket.inventoryservice.dto.ResponseInventoryDto;
import com.urbanmarket.inventoryservice.exception.InventoryGenericException;
import com.urbanmarket.inventoryservice.model.Inventory;
import com.urbanmarket.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
		if (inventoryRepository.existsByProductId(inventoryDto.getProductId())) {
			throw new InventoryGenericException("IVNERROCC0001",
					"Product already exists cannot create a product with productId: " + inventoryDto.getProductId(),
					null);
		}
		Inventory inventory = modelMapper.map(inventoryDto, Inventory.class);
		log.info("Creating a new inventory: " + inventory);
		ResponseInventoryDto savedInventory = convertToResponseInventoryDto(inventoryRepository.save(inventory));
		return savedInventory;
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
				throw new InventoryGenericException("IVNERROCC0002",
						"Inventory not found for given productId: " + productId, null);
			}
			inventoryList = Collections.singletonList(convertToResponseInventoryDto(inventory));
		} else {
			inventoryList = inventoryRepository.findAll().stream().map(this::convertToResponseInventoryDto).toList();
		}
		if (!inventoryList.isEmpty()) {
			return inventoryList;
		} else {
			throw new InventoryGenericException("IVNERROCC0002", "No Inventory available", null);
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
			throw new InventoryGenericException("IVNERROCC0002",
					"No such inventory found for Id: " + inventoryDto.getProductId(), null);
		}
		Inventory savedInventory = inventoryRepository.findByProductId(inventoryDto.getProductId());
		if (savedInventory.equals(modelMapper.map(inventoryDto, Inventory.class))) {
			throw new InventoryGenericException("IVNERROCC0001", "No changes found", null);
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
		ResponseInventoryDto updatedInventory = convertToResponseInventoryDto(inventoryRepository.save(savedInventory));
		return updatedInventory;
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
		inventoryRepository.deleteByProductId(productId);
	}

	/**
	 * Get inventory by product id 
	 * TODO: handle id not found exception
	 * 
	 * @param id productId
	 * @return responseInventoryDto
	 */
	public ResponseInventoryDto getByProductId(String id) {
		return modelMapper.map(inventoryRepository.findByProductId(id), ResponseInventoryDto.class);
	}
}
