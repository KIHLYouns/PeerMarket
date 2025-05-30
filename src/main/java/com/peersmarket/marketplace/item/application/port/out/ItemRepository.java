package com.peersmarket.marketplace.item.application.port.out;

import java.util.List;
import java.util.Optional;

import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.item.domain.model.ItemStatus;

public interface ItemRepository {
    Item save(Item item);
    Item saveAndFlush(Item item);
    Optional<Item> findById(Long id);
    List<Item> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    List<Item> findBySellerId(Long sellerId);
    List<Item> findByCategoryId(Long categoryId);
    List<Item> findByTitleContaining(String title);
    List<Item> findByStatusOrderByCreatedAtDesc(ItemStatus status);
    List<Item> findByStatusOrderBySellerAverageRatingDesc(ItemStatus status);
    List<Item> findByStatusOrderByViewCountDescCreatedAtDesc(ItemStatus status);
}
