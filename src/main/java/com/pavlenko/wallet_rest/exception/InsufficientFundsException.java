package com.pavlenko.wallet_rest.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Insufficient Funds");
    }

    public InsufficientFundsException(String message) {
        super(message);
    }
}
