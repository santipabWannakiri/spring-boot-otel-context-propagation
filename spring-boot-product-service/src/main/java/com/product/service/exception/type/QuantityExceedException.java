package com.product.service.exception.type;

public class QuantityExceedException extends RuntimeException{
    public QuantityExceedException(String message) {
        super(message);
    }
}
