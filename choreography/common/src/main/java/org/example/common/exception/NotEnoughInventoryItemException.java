package org.example.common.exception;

public class NotEnoughInventoryItemException extends Exception {

    public NotEnoughInventoryItemException() {
    }

    public NotEnoughInventoryItemException(String message) {
        super(message);
    }

    public NotEnoughInventoryItemException(String message, Throwable cause) {
        super(message, cause);
    }
}
