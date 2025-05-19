package com.peersmarket.marketplace.review.application.port.in;

import java.util.List;
import java.util.Optional;

import com.peersmarket.marketplace.review.application.dto.CreateReviewRequestDto;
import com.peersmarket.marketplace.review.application.dto.ReviewDto;

public interface ReviewService {
    ReviewDto createReview(final CreateReviewRequestDto createReviewDto);
    Optional<ReviewDto> getReviewById(final Long reviewId);
    List<ReviewDto> getReviewsForUser(final Long userId); // Avis reçus par l'utilisateur (reviewee)
    List<ReviewDto> getReviewsByReviewer(final Long reviewerId); // Avis écrits par l'utilisateur
}