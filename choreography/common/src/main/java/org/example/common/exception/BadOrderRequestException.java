package org.example.common.exception;

public class BadOrderRequestException extends Exception {

    public BadOrderRequestException() {
    }

    public BadOrderRequestException(String message) {
        super(message);
    }

    public BadOrderRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
