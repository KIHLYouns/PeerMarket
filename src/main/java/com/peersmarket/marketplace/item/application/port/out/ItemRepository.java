package com.peersmarket.marketplace.item.application.port.out;

import java.util.List;
import java.util.Optional;

import com.peersmarket.marketplace.item.domain.model.Item;

public interface ItemRepository {
    Item save(Item item);
    Optional<Item> findById(Long id);
    List<Item> findAll();
    void deleteById(Long id);
    List<Item> findBySellerId(Long sellerId);
    List<Item> findByCategoryId(Long categoryId);
    List<Item> findByTitleContaining(String title);
    boolean existsById(Long id);
}
