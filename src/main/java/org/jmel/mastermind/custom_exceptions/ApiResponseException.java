package org.jmel.mastermind.custom_exceptions;

public class ApiResponseException extends RuntimeException {
    public ApiResponseException(String message) {
        super(message);
    }
}
