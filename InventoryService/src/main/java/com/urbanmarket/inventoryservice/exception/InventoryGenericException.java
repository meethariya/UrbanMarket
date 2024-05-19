package com.urbanmarket.inventoryservice.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class InventoryGenericException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1124L;
    private final String errorCode;
    private final String errorMessage;
    /**
     * Default constructor
     */
    public InventoryGenericException() {
        super();
        this.errorCode=null;
        this.errorMessage=null;
    }

    /**
     * Constructor with error message.
     * @param message error
     */
    public InventoryGenericException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

}
