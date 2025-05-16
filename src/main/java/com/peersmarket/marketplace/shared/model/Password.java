package com.peersmarket.marketplace.shared.model;

import com.peersmarket.marketplace.shared.exception.InvalidPasswordException;

public record Password(String value) {
    public Password {
        if (value == null || value.isBlank()) {
            throw new InvalidPasswordException("Password cannot be null or blank");
        }
        if (value.length() < 8) {
            throw new InvalidPasswordException("Password must be at least 8 characters long");
        }
        
    }
}