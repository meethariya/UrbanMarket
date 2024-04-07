/**
 * 07-Apr-2024
 * meeth
 */
package com.urbanmarket.reviewservice.controller;

import java.io.IOException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.urbanmarket.reviewservice.exception.ReviewNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles all exceptions
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

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
	 * Handles DuplicateKeyException with status 400
	 * 
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<String> handleDuplicateKeyException(DuplicateKeyException e) {
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

	/**
	 * Handles ReviewNotFoundException with status 404
	 * 
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(ReviewNotFoundException.class)
	public ResponseEntity<String> handleReviewNotFoundException(ReviewNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
}
