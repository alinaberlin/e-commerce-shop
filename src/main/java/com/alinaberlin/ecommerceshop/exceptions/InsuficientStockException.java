package com.alinaberlin.ecommerceshop.exceptions;

public class InsuficientStockException extends RuntimeException {
    public InsuficientStockException(String message) {
        super(message);
    }

    public InsuficientStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsuficientStockException(Throwable cause) {
        super(cause);
    }
}
