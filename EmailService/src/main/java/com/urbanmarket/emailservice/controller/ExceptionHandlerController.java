/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.emailservice.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.urbanmarket.emailservice.exception.ServiceUnavailableException;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles all exceptions
 */
@RestControllerAdvice
@Slf4j
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
	 * Handles ServiceUnavailableException with status 500
	 * 
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(ServiceUnavailableException.class)
	public ResponseEntity<String> handleServiceUnavailableException(ServiceUnavailableException e) {
		log.error(e.getMessage(), e);
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handles error for MailException with status 400
	 * 
	 * @param e exception
	 * @return Error Message response
	 */
	@ExceptionHandler(MailException.class)
	public ResponseEntity<String> handleMailException(MailException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles error for MessagingException with status 400
	 * 
	 * @param e exception
	 * @return Error Message response
	 */
	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<String> handleMessagingException(MessagingException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
