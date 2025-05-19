package com.peersmarket.marketplace.review.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peersmarket.marketplace.review.infrastructure.persistence.jpa.model.ReviewEntity;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {

    // Utiliser JOIN FETCH pour charger les utilisateurs associés et éviter N+1 problèmes
    @Query("SELECT r FROM ReviewEntity r JOIN FETCH r.reviewer JOIN FETCH r.reviewee WHERE r.id = :id")
    @Override
    Optional<ReviewEntity> findById(@Param("id") Long id);

    @Query("SELECT r FROM ReviewEntity r JOIN FETCH r.reviewer JOIN FETCH r.reviewee WHERE r.reviewee.id = :revieweeId")
    List<ReviewEntity> findByRevieweeIdWithUsers(@Param("revieweeId") Long revieweeId);

    @Query("SELECT r FROM ReviewEntity r JOIN FETCH r.reviewer JOIN FETCH r.reviewee WHERE r.reviewer.id = :reviewerId")
    List<ReviewEntity> findByReviewerIdWithUsers(@Param("reviewerId") Long reviewerId);
}