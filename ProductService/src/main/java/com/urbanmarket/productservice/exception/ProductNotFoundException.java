/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.productservice.exception;

/**
 * Throw runtime exception when product dosen't exist
 */
public class ProductNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ProductNotFoundException() {
		super();
	}

	/**
	 * @param message
	 */
	public ProductNotFoundException(String message) {
		super(message);
	}
	
	
}
