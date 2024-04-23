package com.urbanmarket.inventoryservice.exception;

import lombok.Getter;

import java.io.Serial;

public class InventoryGenericException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1124L;
    @Getter
    private String errorCode;
    private String errorMessage;
    /**
     * Default constructor
     */
    public InventoryGenericException() {
        super();
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

    /**
     * Constructor with error code
     * @param cause
     * @param errorCode
     */
    public InventoryGenericException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

}
