package com.peersmarket.marketplace.item.application.port.in;

import java.util.List;

import com.peersmarket.marketplace.item.application.dto.ItemDto;

public interface ItemSuggestionService {
    List<ItemDto> getSuggestedItems();
    List<ItemDto> getSuggestedItems(String strategyName);
}
