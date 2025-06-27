package com.pavlenko.wallet_rest.exception_handling;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException() {
        super("Invalid operation");
    }

    public InvalidOperationException(String message) {
        super(message);
    }
}
