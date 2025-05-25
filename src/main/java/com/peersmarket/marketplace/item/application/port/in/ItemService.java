package com.peersmarket.marketplace.item.application.port.in;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.peersmarket.marketplace.item.application.dto.CreateItemDto;
import com.peersmarket.marketplace.item.application.dto.ImageDto;
import com.peersmarket.marketplace.item.application.dto.ItemDto;

public interface ItemService {
    ItemDto createItem(CreateItemDto itemDto);
    Optional<ItemDto> getItemById(Long id);
    List<ItemDto> getAllItems();
    List<ItemDto> getItemsBySellerId(Long sellerId);
    List<ItemDto> getItemsByCategoryId(Long categoryId);
    List<ItemDto> searchItemsByTitle(String title);
    ItemDto updateItem(Long id, ItemDto itemDto);
    void deleteItem(Long id);

    ImageDto addImageToItem(Long itemId, MultipartFile imageFile);
    ItemDto addImagesToItem(Long itemId, List<MultipartFile> imageFiles);
    void deleteItemImage(Long itemId, Long imageId);
    List<ImageDto> getItemImages(Long itemId);
}
