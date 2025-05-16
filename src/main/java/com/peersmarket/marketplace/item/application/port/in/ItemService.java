package com.peersmarket.marketplace.item.application.port.in;

import java.util.List;
import java.util.Optional;

import com.peersmarket.marketplace.item.application.dto.ItemDto;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto);
    Optional<ItemDto> getItemById(Long id);
    List<ItemDto> getAllItems();
    List<ItemDto> getItemsBySellerId(Long sellerId);
    List<ItemDto> getItemsByCategoryId(Long categoryId);
    List<ItemDto> searchItemsByTitle(String title);
    ItemDto updateItem(Long id, ItemDto itemDto);
    void deleteItem(Long id);
}
