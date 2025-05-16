package com.peersmarket.marketplace.shared.model;

import com.peersmarket.marketplace.shared.exception.InvalidEmailException;

public record Email(String value) {
    public Email {
        if (value == null || value.isBlank()) {
            throw new InvalidEmailException("Email cannot be null or blank");
        }
        if (!value.matches("^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$")) {
            throw new InvalidEmailException("Invalid email format");
        }
    }
}