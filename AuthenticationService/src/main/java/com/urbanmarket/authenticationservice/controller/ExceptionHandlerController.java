/**
 * 14-Apr-2024
 * meeth
 */
package com.urbanmarket.authenticationservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.urbanmarket.authenticationservice.exception.CredentialNotFoundException;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller to handle all errors.
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

	/**
	 * Handles error for {@link UserCredentialNotFoundException}.
	 * 
	 * @param e Error
	 * @return Error message with Not Found status.
	 */
	@ExceptionHandler(CredentialNotFoundException.class)
	public ResponseEntity<String> handleCredentialNotFoundException(CredentialNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles error for {@link BadCredentialsException}.
	 * 
	 * @param e Error
	 * @return Error message with UnAuthorized status.
	 */
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Handles error for {@link SignatureException}.
	 * 
	 * @param e Error
	 * @return Error message with UnAuthorized status.
	 */
	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<String> handleSignatureException(SignatureException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Handles error for {@link MalformedJwtException}.
	 * 
	 * @param e Error
	 * @return Error message with UnAuthorized status.
	 */
	@ExceptionHandler(MalformedJwtException.class)
	public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}

}
