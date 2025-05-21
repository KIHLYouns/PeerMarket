package com.peersmarket.marketplace.saveditem.application.port.in;

import java.util.List;

import com.peersmarket.marketplace.saveditem.application.dto.CreateSavedItemDto;
import com.peersmarket.marketplace.saveditem.application.dto.SavedItemDto;

public interface SavedItemService {
    SavedItemDto saveItem(CreateSavedItemDto createSavedItemDto);
    void unsaveItem(Long userId, Long itemId);
    List<SavedItemDto> getSavedItemsByUserId(Long userId);
    boolean isItemSavedByUser(Long userId, Long itemId);
}
