package com.peersmarket.marketplace.review.application.service;

import com.peersmarket.marketplace.review.application.dto.CreateReviewRequestDto;
import com.peersmarket.marketplace.review.application.dto.ReviewDto;
import com.peersmarket.marketplace.review.application.port.in.ReviewService;
import com.peersmarket.marketplace.review.application.port.out.ReviewRepository;
import com.peersmarket.marketplace.review.domain.exception.ReviewNotFoundException;
import com.peersmarket.marketplace.review.domain.model.Review;
import com.peersmarket.marketplace.review.infrastructure.persistence.jpa.mapper.ReviewMapper;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository; // Assurez-vous que le chemin est correct
import com.peersmarket.marketplace.user.domain.model.AppUser; // Assurez-vous que le chemin est correct
import com.peersmarket.marketplace.shared.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final AppUserRepository appUserRepository; // Pour récupérer les objets AppUser
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewDto createReview(final CreateReviewRequestDto createReviewDto) {
        final AppUser reviewer = appUserRepository.findById(createReviewDto.getReviewerId())
                .orElseThrow(() -> new NotFoundException("Reviewer not found with id: " + createReviewDto.getReviewerId()));
        final AppUser reviewee = appUserRepository.findById(createReviewDto.getRevieweeId())
                .orElseThrow(() -> new NotFoundException("Reviewee not found with id: " + createReviewDto.getRevieweeId()));

        if (reviewer.getId().equals(reviewee.getId())) {
            throw new IllegalArgumentException("Users cannot review themselves.");
        }

        // Optionnel : Vérifier si un avis existe déjà (selon vos règles métier)
        // Par exemple, un utilisateur ne peut évaluer un autre qu'une seule fois.
        // List<Review> existingReviews = reviewRepository.findByReviewerId(reviewerId);
        // if (existingReviews.stream().anyMatch(r -> r.getReviewee().getId().equals(reviewee.getId()))) {
        //     throw new IllegalArgumentException("You have already reviewed this user.");
        // }


        final Review review = Review.builder()
                .rating(createReviewDto.getRating())
                .comment(createReviewDto.getComment())
                .reviewer(reviewer)
                .reviewee(reviewee)
                .createdAt(LocalDateTime.now()) // Géré par @CreationTimestamp dans l'entité mais peut être défini ici aussi
                .build();

        final Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDto(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewDto> getReviewById(final Long reviewId) {
        return reviewRepository.findById(reviewId).map(reviewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsForUser(final Long userId) {
        // Vérifier si l'utilisateur existe
        if (!appUserRepository.existsById(userId)) {
            throw new NotFoundException("User (reviewee) not found with id: " + userId);
        }
        return reviewRepository.findByRevieweeId(userId).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByReviewer(final Long reviewerId) {
         // Vérifier si l'utilisateur existe
        if (!appUserRepository.existsById(reviewerId)) {
            throw new NotFoundException("User (reviewer) not found with id: " + reviewerId);
        }
        return reviewRepository.findByReviewerId(reviewerId).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }
}