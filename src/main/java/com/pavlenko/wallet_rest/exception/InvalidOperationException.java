package com.pavlenko.wallet_rest.exception;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException() {
        super("Invalid operation");
    }

    public InvalidOperationException(String message) {
        super(message);
    }
}
