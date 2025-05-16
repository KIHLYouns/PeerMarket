package com.peersmarket.marketplace.shared.exception;

public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException(final String message) {
        super(message);
    }
}
