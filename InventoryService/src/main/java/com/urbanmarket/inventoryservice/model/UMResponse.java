package com.urbanmarket.inventoryservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class UMResponse {
    private ResponseStatus status;
    private UMErrorResponse errorResponse;
    private Object data;

    public enum ResponseStatus{
        SUCCESS,
        FAILURE,
        PENDING
    }
}
