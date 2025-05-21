package com.peersmarket.marketplace.saveditem.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.peersmarket.marketplace.saveditem.infrastructure.persistence.jpa.model.SavedItemEntity;

@Repository
public interface SavedItemJpaRepository extends JpaRepository<SavedItemEntity, Long> {
    List<SavedItemEntity> findByUserId(Long userId);
    Optional<SavedItemEntity> findByUserIdAndItemId(Long userId, Long itemId);
    void deleteByUserIdAndItemId(Long userId, Long itemId);
    boolean existsByUserIdAndItemId(Long userId, Long itemId);
}