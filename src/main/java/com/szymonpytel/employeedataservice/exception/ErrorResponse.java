package com.szymonpytel.employeedataservice.exception;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(int status, String message, Map<String, String> errors) {

    public ErrorResponse(int status, String message) {
        this(status, message, null);
    }
}