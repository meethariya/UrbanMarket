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
	 * Auto generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public ProductNotFoundException() {
		super();
	}

	/**
	 * Constructor with error message.
	 * @param message error
	 */
	public ProductNotFoundException(String message) {
		super(message);
	}
	
	
}
