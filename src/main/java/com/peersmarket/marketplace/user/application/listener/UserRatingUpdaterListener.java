package com.peersmarket.marketplace.user.application.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.peersmarket.marketplace.review.application.event.ReviewCreatedEvent;
import com.peersmarket.marketplace.review.application.port.out.ReviewRepository;
import com.peersmarket.marketplace.review.domain.model.Review;
import com.peersmarket.marketplace.shared.exception.NotFoundException;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;
import com.peersmarket.marketplace.user.domain.model.AppUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRatingUpdaterListener {

    private final AppUserRepository appUserRepository;
    private final ReviewRepository reviewRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void handleReviewCreatedEvent(final ReviewCreatedEvent event) {
        log.info("Received ReviewCreatedEvent for revieweeId: {}", event.getRevieweeId());

        final Long revieweeId = event.getRevieweeId();

        final AppUser user = appUserRepository.findById(revieweeId)
                .orElseThrow(() -> {
                    log.error("User (reviewee) not found with id: {} while handling ReviewCreatedEvent", revieweeId);
                    return new NotFoundException("User (reviewee) not found with id: " + revieweeId);
                });

        // Récupérer tous les avis pour cet utilisateur
        final List<Review> reviewsForUser = reviewRepository.findByRevieweeId(revieweeId);

        if (reviewsForUser.isEmpty()) {
            user.setRatingCount(0);
            user.setAverageRating(0.0);
            log.info("No reviews found for user {}. Setting rating count to 0 and average rating to 0.0.", revieweeId);
        } else {
            final List<Integer> ratings = reviewsForUser.stream()
                    .map(Review::getRating)
                    .collect(Collectors.toList());
            
            user.recalculateRating(ratings);
            log.info("Updated ratings for user {}: Count = {}, Average = {}", 
                    revieweeId, user.getRatingCount(), user.getAverageRating());
        }

        appUserRepository.save(user);
        log.info("Successfully updated and saved rating statistics for user {}", revieweeId);
    }
}
