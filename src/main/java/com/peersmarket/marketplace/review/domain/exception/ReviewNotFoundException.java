package com.peersmarket.marketplace.review.domain.exception;

// Ou étendez une exception de base de votre projet si elle existe
public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(final String message) {
        super(message);
    }
}
