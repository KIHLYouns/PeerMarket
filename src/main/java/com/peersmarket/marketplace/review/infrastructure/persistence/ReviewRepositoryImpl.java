package com.peersmarket.marketplace.review.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.review.application.port.out.ReviewRepository;
import com.peersmarket.marketplace.review.domain.model.Review;
import com.peersmarket.marketplace.review.infrastructure.persistence.jpa.mapper.ReviewMapper;
import com.peersmarket.marketplace.review.infrastructure.persistence.jpa.model.ReviewEntity;
import com.peersmarket.marketplace.review.infrastructure.persistence.jpa.repository.ReviewJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public Review save(final Review review) {
        ReviewEntity reviewEntity = reviewMapper.toEntity(review);
        return reviewMapper.toDomain(reviewJpaRepository.save(reviewEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> findById(final Long reviewId) {
        return reviewJpaRepository.findById(reviewId).map(reviewMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findByRevieweeId(final Long revieweeId) {
        return reviewJpaRepository.findByRevieweeIdWithUsers(revieweeId).stream()
                .map(reviewMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findByReviewerId(final Long reviewerId) {
        return reviewJpaRepository.findByReviewerIdWithUsers(reviewerId).stream()
                .map(reviewMapper::toDomain)
                .collect(Collectors.toList());
    }
}
