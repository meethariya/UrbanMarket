/**
 * 10-Apr-2024
 * anike
 */
package com.urbanmarket.inventoryservice.controller;

import com.urbanmarket.inventoryservice.exception.ExceptionMessage;
import com.urbanmarket.inventoryservice.exception.InventoryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

/**
 * Handles all the {@link Exception}s.
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerController {


	private final MessageSource source;
	/**
	 * Handles inventory not found exception with status 404
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(InventoryNotFoundException.class)
	public ResponseEntity<ExceptionMessage> handleProductNotFoundException(InventoryNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionResponseGenerator("notFound.code", "notFound.title", e.getMessage()),
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles invalid user input with status 400
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(
				exceptionResponseGenerator("invalidInput.code", "invalidInput.title", e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles DuplicateKeyException with status 400
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<ExceptionMessage> handleDuplicateKeyException(DuplicateKeyException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(
				exceptionResponseGenerator("invalidInput.code", "invalidInput.title", e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles IOException with status 500
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(IOException.class)
	public ResponseEntity<ExceptionMessage> handleIOException(IOException e) {
		log.error(e.getMessage(), e);
		return new ResponseEntity<>(exceptionResponseGenerator("ioException.code", "ioException.title", e.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}


	/**
	 * Handles Exception with status 500
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionMessage> handleException(Exception e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(
				exceptionResponseGenerator("internalServerError.code", "internalServerError.title", e.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
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
