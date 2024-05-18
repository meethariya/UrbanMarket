/**
 * 14-Apr-2024
 * meeth
 */
package com.urbanmarket.authenticationservice.controller;

import java.util.Date;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.urbanmarket.authenticationservice.exception.CredentialNotFoundException;
import com.urbanmarket.authenticationservice.exception.ExceptionMessage;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller to handle all errors.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerController {

	private final MessageSource source;
	private static final String UNAUTHORIZEDCODE = "unauthorized.code";
	private static final String UNAUTHORIZEDTITLE = "unauthorized.title";

	/**
	 * Handles error for {@link UserCredentialNotFoundException}.
	 * 
	 * @param e Error
	 * @return Error message with Not Found status.
	 */
	@ExceptionHandler(CredentialNotFoundException.class)
	public ResponseEntity<ExceptionMessage> handleCredentialNotFoundException(CredentialNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionResponseGenerator("notFound.code", "notFound.title", e.getMessage()),
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles DataIntegrityViolationException with status 400
	 * 
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ExceptionMessage> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(
				exceptionResponseGenerator("invalidInput.code", "invalidInput.title", e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles error for {@link BadCredentialsException}.
	 * 
	 * @param e Error
	 * @return Error message with UnAuthorized status.
	 */
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionMessage> handleBadCredentialsException(BadCredentialsException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionResponseGenerator(UNAUTHORIZEDCODE, UNAUTHORIZEDTITLE, e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles error for {@link SignatureException}.
	 * 
	 * @param e Error
	 * @return Error message with UnAuthorized status.
	 */
	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<ExceptionMessage> handleSignatureException(SignatureException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionResponseGenerator(UNAUTHORIZEDCODE, UNAUTHORIZEDTITLE, e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles error for {@link MalformedJwtException}.
	 * 
	 * @param e Error
	 * @return Error message with UnAuthorized status.
	 */
	@ExceptionHandler(MalformedJwtException.class)
	public ResponseEntity<ExceptionMessage> handleMalformedJwtException(MalformedJwtException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionResponseGenerator(UNAUTHORIZEDCODE, UNAUTHORIZEDTITLE, e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Generate Response for exception
	 * 
	 * @param code    exception status code
	 * @param title   exception title
	 * @param message exception message
	 * @return exceptionMessage
	 */
	private ExceptionMessage exceptionResponseGenerator(String code, String title, String message) {
		return ExceptionMessage.builder().timestamp(new Date()).status(source.getMessage(code, null, Locale.ENGLISH))
				.title(source.getMessage(title, null, Locale.ENGLISH)).message(message).build();
	}

}
