/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.emailservice.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.urbanmarket.emailservice.exception.ExceptionMessage;
import com.urbanmarket.emailservice.exception.ServiceUnavailableException;

import jakarta.mail.MessagingException;
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
	 * Handles IOException with status 500
	 * 
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
	 * Handles ServiceUnavailableException with status 500
	 * 
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(ServiceUnavailableException.class)
	public ResponseEntity<Object> handleServiceUnavailableException(ServiceUnavailableException e) {
		log.error(e.getMessage(), e);
		if (e.getCode() != null) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(e.getCode()));
		}
		return new ResponseEntity<>(
				exceptionResponseGenerator("serviceUnavailable.code", "serviceUnavailable.title", e.getMessage()),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	/**
	 * Handles error for MailException with status 400
	 * 
	 * @param e exception
	 * @return Error Message response
	 */
	@ExceptionHandler(MailException.class)
	public ResponseEntity<ExceptionMessage> handleMailException(MailException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(
				exceptionResponseGenerator("mailException.code", "mailException.title", e.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handles error for MessagingException with status 400
	 * 
	 * @param e exception
	 * @return Error Message response
	 */
	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<ExceptionMessage> handleMessagingException(MessagingException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(
				exceptionResponseGenerator("mailException.code", "mailException.title", e.getMessage()),
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
