/**
 * 28-Apr-2024
 * meeth
 */
package com.urbanmarket.productservice.exception;

/**
 * Throw this exception when other service is unavailable
 */
public class ServiceUnavailableException extends Exception {
	/**
	 * Auto generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public ServiceUnavailableException() {
		super();
	}

	/**
	 * Constructor with error message
	 * 
	 * @param message error
	 */
	public ServiceUnavailableException(String message) {
		super(message);
	}
}
