/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.orderservice.controller;

import java.util.Date;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.urbanmarket.orderservice.exception.DataNotFoundException;
import com.urbanmarket.orderservice.exception.ExceptionMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles all exceptions
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerController {

	private final MessageSource source;

	/**
	 * Handles data not found exception with status 404
	 * 
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ExceptionMessage> handleDataNotFoundException(DataNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(exceptionResponseGenerator("notFound.code", "notFound.title", e.getMessage()),
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles invalid user input with status 400
	 * 
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
