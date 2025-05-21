package com.peersmarket.marketplace.saveditem.application.port.out;

import java.util.List;
import java.util.Optional;

import com.peersmarket.marketplace.saveditem.domain.model.SavedItem;

public interface SavedItemRepository {
    SavedItem save(SavedItem savedItem);
    Optional<SavedItem> findById(Long id);
    List<SavedItem> findByUserId(Long userId);
    Optional<SavedItem> findByUserIdAndItemId(Long userId, Long itemId);
    void deleteByUserIdAndItemId(Long userId, Long itemId);
    boolean existsByUserIdAndItemId(Long userId, Long itemId);
    void deleteById(Long id);
}
