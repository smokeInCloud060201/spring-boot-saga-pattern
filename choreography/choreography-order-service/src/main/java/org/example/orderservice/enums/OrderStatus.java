package org.example.orderservice.enums;

public enum OrderStatus {
    CREATED,
    CHECKOUT,
    PROGRESSING_PAYMENT,
    USER_CANCEL,
    COMPLETED,
    NO_INVENTORY,
    PAYMENT_ERROR
}
