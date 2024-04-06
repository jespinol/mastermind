package org.jmel.mastermind.custom_exceptions;

public class ApiQuotaExceededException extends Exception {
    public ApiQuotaExceededException(String message) {
        super(message);
    }
}
