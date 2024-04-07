/**
 * 07-Apr-2024
 * meeth
 */
package com.urbanmarket.reviewservice.exception;

/**
 * Throw this exception when review is not found.
 */
public class ReviewNotFoundException extends RuntimeException {

	/**
	 * Default generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public ReviewNotFoundException() {
		super();
	}

	/**
	 * Constructor with message
	 * 
	 * @param message error
	 */
	public ReviewNotFoundException(String message) {
		super(message);
	}

}
