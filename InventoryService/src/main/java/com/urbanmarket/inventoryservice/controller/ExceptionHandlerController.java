/**
 * 10-Apr-2024
 * anike
 */
package com.urbanmarket.inventoryservice.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.urbanmarket.inventoryservice.component.ErrorPropertyReader;
import com.urbanmarket.inventoryservice.exception.InventoryGenericException;
import com.urbanmarket.inventoryservice.model.UMErrorResponse;
import com.urbanmarket.inventoryservice.model.UMResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.urbanmarket.inventoryservice.exception.InventoryNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles all the {@link Exception}s.
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

	@Autowired
	private ErrorPropertyReader errorPropertyReader;
	/**
	 * Handles inventory not found exception with status 404
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(InventoryNotFoundException.class)
	public ResponseEntity<String> handleProductNotFoundException(InventoryNotFoundException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles invalid user input with status 400
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
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> handleIOException(IOException e) {
		log.error(e.getMessage(),e);
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handles {@link MethodArgumentNotValidException}
	 * @param e exception
	 * @return error message
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
		log.error(e.getMessage(),e);
		Map<String, Object> errors = new HashMap<>();
		e.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});
		return new ResponseEntity<>(errors,e.getStatusCode());
	}
	@ExceptionHandler(InventoryGenericException.class)
	public ResponseEntity<UMErrorResponse> handleInventoryGenericException(InventoryGenericException e){
		log.error(e.getMessage(),e);
		String errorCodeFromException = e.getErrorCode();
		String errorMessageFromException = e.getMessage();
		int errorStatusValue = this.errorPropertyReader.getErrorStatusValue(errorCodeFromException);
		String errorMessageValue = this.errorPropertyReader.getErrorCodeMessageValue(errorCodeFromException);
		String outgoingErrorMessage = this.errorPropertyReader.getErrorCodeMessageValue("Internal Server Error");
		if(errorMessageValue == null || errorMessageValue.isEmpty()){
			// no error errorMessage value provided to override. Using the incoming value from exception itself.
			if(errorCodeFromException != null || !errorCodeFromException.isEmpty()){
				outgoingErrorMessage = errorMessageFromException;
			}
		}else{
			// Mapping error message exist in the mapping file... Overriding
			outgoingErrorMessage = errorMessageValue;
		}
		UMErrorResponse errorResponse = UMErrorResponse.builder().errorCode(errorCodeFromException).errorMessage(outgoingErrorMessage).build();
		ResponseEntity responseEntity = ResponseEntity.status(errorStatusValue).body(UMResponse.builder().status(UMResponse.ResponseStatus.FAILURE).data(errorResponse).build());
		return responseEntity;
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception ex) {
		return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
