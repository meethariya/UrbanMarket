/**
 * 21-Apr-2024
 * meeth
 */
package com.urbanmarket.orderservice.exception;

/**
 * Throw this exception with status 404 when data is not found
 */
public class DataNotFoundException extends RuntimeException {

	/**
	 * Default generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public DataNotFoundException() {
		super();
	}

	/**
	 * Constructor with error message
	 * 
	 * @param message error
	 */
	public DataNotFoundException(String message) {
		super(message);
	}

}
