/**
 * 09-Apr-2024
 * meeth
 */
package com.urbanmarket.userservice.exception;

/**
 * Throw this runtime exception when user is not found.
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
	 * Constructor with error message.
	 * 
	 * @param message error
	 */
	public DataNotFoundException(String message) {
		super(message);
	}

}
