package com.peersmarket.marketplace.review.application.port.out;

import java.util.List;
import java.util.Optional;

import com.peersmarket.marketplace.review.domain.model.Review;

public interface ReviewRepository {
    Review save(final Review review);
    Optional<Review> findById(final Long reviewId);
    List<Review> findByRevieweeId(final Long revieweeId);
    List<Review> findByReviewerId(final Long reviewerId);
    // boolean existsById(final Long reviewId); // Si nécessaire pour des validations
}
