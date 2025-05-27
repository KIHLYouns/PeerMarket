package com.peersmarket.marketplace.item.infrastructure.persistence.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.peersmarket.marketplace.item.domain.model.ItemStatus;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.model.ItemEntity;

@Repository
public interface ItemJpaRepository extends JpaRepository<ItemEntity, Long> {
    List<ItemEntity> findBySellerId(Long sellerId);
    List<ItemEntity> findByCategoryId(Long categoryId);
    List<ItemEntity> findByTitleContainingIgnoreCase(String title);
    List<ItemEntity> findByStatusOrderByCreatedAtDesc(ItemStatus status);

}
