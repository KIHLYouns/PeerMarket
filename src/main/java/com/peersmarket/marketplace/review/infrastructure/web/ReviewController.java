package com.peersmarket.marketplace.review.infrastructure.web;

import com.peersmarket.marketplace.review.application.dto.CreateReviewRequestDto;
import com.peersmarket.marketplace.review.application.dto.ReviewDto;
import com.peersmarket.marketplace.review.application.port.in.ReviewService;
import com.peersmarket.marketplace.shared.exception.NotFoundException;
// Importer votre UserPrincipal si vous en avez un personnalisé
// import com.peersmarket.marketplace.common.security.UserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails; // Ou votre UserPrincipal
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody final CreateReviewRequestDto createReviewDto,
                                                  @AuthenticationPrincipal UserDetails currentUser) {
        try {
            final ReviewDto createdReview = reviewService.createReview(createReviewDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
        } catch (NotFoundException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        // D'autres exceptions (ex: DataIntegrityViolationException si une contrainte unique est violée)
        // peuvent être gérées par le GlobalExceptionHandler.
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable final Long reviewId) {
        return reviewService.getReviewById(reviewId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found with id: " + reviewId));
    }

    // Obtenir les avis reçus par un utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviewsForUser(@PathVariable final Long userId) {
        try {
            final List<ReviewDto> reviews = reviewService.getReviewsForUser(userId);
            return ResponseEntity.ok(reviews);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Obtenir les avis écrits par un utilisateur
    @GetMapping("/by/{reviewerId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByReviewer(@PathVariable final Long reviewerId) {
         try {
            final List<ReviewDto> reviews = reviewService.getReviewsByReviewer(reviewerId);
            return ResponseEntity.ok(reviews);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}