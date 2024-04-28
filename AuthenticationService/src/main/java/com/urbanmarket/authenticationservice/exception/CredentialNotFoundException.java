/**
 * 14-Apr-2024
 * meeth
 */
package com.urbanmarket.authenticationservice.exception;

/**
 * Throw this exception when user credential is not found
 */
public class CredentialNotFoundException extends RuntimeException {

	/**
	 * Default generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public CredentialNotFoundException() {
		super();
	}

	/**
	 * Constructor with error message
	 * 
	 * @param message error
	 */
	public CredentialNotFoundException(String message) {
		super(message);
	}

}
