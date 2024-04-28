/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.userservice.controller;

import java.io.IOException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.urbanmarket.userservice.exception.DataNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles all exceptions
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
	/**
	 * Handles data not found exception with status 404
	 * 
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<String> handleDataNotFoundException(DataNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles invalid user input with status 400
	 * 
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles DataIntegrityViolationException with status 400
	 * 
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles IOException with status 500
	 * 
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> handleIOException(IOException e) {
		log.error(e.getMessage(), e);
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
