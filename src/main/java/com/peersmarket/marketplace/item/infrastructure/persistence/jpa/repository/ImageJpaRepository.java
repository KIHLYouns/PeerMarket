package com.peersmarket.marketplace.item.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.model.ImageEntity;

@Repository
public interface ImageJpaRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByItemId(Long itemId);
    Optional<ImageEntity> findByIdAndItemId(Long id, Long itemId);
    void deleteByIdAndItemId(Long id, Long itemId); // Peut être implémenté par Spring Data JPA ou nécessiter @Modifying @Query
}
