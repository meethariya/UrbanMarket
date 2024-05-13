/**
 * 13-May-2024
 * meeth
 */
package com.urbanmarket.apigateway.exception;

/**
 * Throw this <b>Runtime Exception</b> when the request is not authorized.
 * 
 * @author MEETKIRTIBHAI
 * @since 04-Oct-2023
 */
public class UnauthorizedException extends RuntimeException {

	/**
	 * Default generated
	 */
	private static final long serialVersionUID = 971622666546629076L;

	/**
	 * Default Constructor
	 */
	public UnauthorizedException() {
		super();
	}

	/**
	 * Constructor with error message
	 * 
	 * @param message error message
	 */
	public UnauthorizedException(String message) {
		super(message);
	}

}
