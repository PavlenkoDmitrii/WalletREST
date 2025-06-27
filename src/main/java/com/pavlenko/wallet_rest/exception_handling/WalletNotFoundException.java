package com.pavlenko.wallet_rest.exception_handling;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException() {
        super("Wallet not found");
    }

    public WalletNotFoundException(String message) {
        super(message);
    }
}
