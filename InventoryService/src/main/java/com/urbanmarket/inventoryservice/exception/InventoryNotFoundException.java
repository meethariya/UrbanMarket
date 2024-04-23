/**
 * 31-Mar-2024
 * anike
 */
package com.urbanmarket.inventoryservice.exception;

import com.urbanmarket.inventoryservice.model.Inventory;

import java.io.Serial;

/**
 * Throw runtime exception when {@link Inventory} dosen't exist
 */
public class InventoryNotFoundException extends RuntimeException{

	/**
	 * Auto generated
	 */
	@Serial
	private static final long serialVersionUID = 1123L;

	/**
	 * Default constructor
	 */
	public InventoryNotFoundException() {
		super();
	}

	/**
	 * Constructor with error message.
	 * @param message error
	 */
	public InventoryNotFoundException(String message) {
		super(message);
	}
	
}
