package com.peersmarket.marketplace.item.application.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.peersmarket.marketplace.item.application.dto.CreateItemDto;
import com.peersmarket.marketplace.item.application.dto.ImageDto;
import com.peersmarket.marketplace.item.application.dto.ItemDto;
import com.peersmarket.marketplace.item.application.model.ImageFileWrapper;
import com.peersmarket.marketplace.item.application.port.in.ItemService;
import com.peersmarket.marketplace.item.application.port.out.CategoryRepository;
import com.peersmarket.marketplace.item.application.port.out.ImageRepository;
import com.peersmarket.marketplace.item.application.port.out.ImageStoragePort;
import com.peersmarket.marketplace.item.application.port.out.ItemRepository;
import com.peersmarket.marketplace.item.domain.model.Category;
import com.peersmarket.marketplace.item.domain.model.Image;
import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.ImageMapper;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.ItemMapper;
import com.peersmarket.marketplace.shared.exception.NotFoundException;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;
import com.peersmarket.marketplace.user.domain.model.AppUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ItemMapper itemMapper;
    private final ImageMapper imageMapper;
    private final ImageStoragePort imageStoragePort;

    @Override
    public ItemDto createItem(final CreateItemDto itemDto) {
        final AppUser seller = appUserRepository.findById(itemDto.getSellerId())
                .orElseThrow(() -> new NotFoundException("Vendeur non trouvé avec l'ID : " + itemDto.getSellerId()));
        final Category category = categoryRepository.findById(itemDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Catégorie non trouvée avec l'ID : " + itemDto.getCategoryId()));

        final Item newItem = Item.builder()
                .title(itemDto.getTitle())
                .description(itemDto.getDescription())
                .price(itemDto.getPrice())
                .condition(itemDto.getCondition())
                .seller(seller)
                .category(category)
                .build();
                
        final Item savedItem = itemRepository.save(newItem);
        
        return itemMapper.toDto(savedItem);
    }

    @Override
    @Transactional
    public Optional<ItemDto> getItemById(final Long id) {
        final Optional<Item> itemOpt = itemRepository.findById(id);
        if (itemOpt.isPresent()) {
            final Item item = itemOpt.get();
            // Incrémenter le compteur de vues
            item.setViewCount((item.getViewCount() == null ? 0 : item.getViewCount()) + 1);
            itemRepository.save(item);
            return Optional.of(itemMapper.toDto(item));
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllItems() {
        return itemRepository.findAll().stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItemsBySellerId(final Long sellerId) {
        return itemRepository.findBySellerId(sellerId).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItemsByCategoryId(final Long categoryId) {
        return itemRepository.findByCategoryId(categoryId).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchItemsByTitle(final String title) {
        return itemRepository.findByTitleContaining(title).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(final Long id, final ItemDto itemDto) {
        final Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Article non trouvé avec l'ID : " + id));

        existingItem.setTitle(itemDto.getTitle());
        existingItem.setDescription(itemDto.getDescription());
        existingItem.setPrice(itemDto.getPrice());
        existingItem.setCondition(itemDto.getCondition());

        if (itemDto.getStatus() != null) {
            existingItem.setStatus(itemDto.getStatus());
        }

        if (itemDto.getCategoryId() != null && !itemDto.getCategoryId().equals(existingItem.getCategory().getId())) {
            final Category newCategory = categoryRepository.findById(itemDto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Nouvelle catégorie non trouvée avec l'ID : " + itemDto.getCategoryId()));
            existingItem.setCategory(newCategory);
        }

        final Item updatedItem = itemRepository.saveAndFlush(existingItem);
        return itemMapper.toDto(updatedItem);
    }

    @Override
    public void deleteItem(final Long id) {
        final Item item = itemRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Article non trouvé avec l'ID : " + id));

        if (item.getImages() != null) {
            for (final Image image : item.getImages()) {
                try {
                    imageStoragePort.deleteImage(image.getUrl());
                } catch (final Exception e) {
                    log.error("Échec de la suppression de l'image {} du stockage externe pour l'item ID {}: {}",
                            image.getUrl(), id, e.getMessage());
                }
            }
        }
        itemRepository.deleteById(id);
    }

    @Override
    public ImageDto addImageToItem(final Long itemId, final MultipartFile imageFile) {
        final Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("Article non trouvé avec l'ID: " + itemId));

        try {
            final ImageFileWrapper fileWrapper = new ImageFileWrapper(
                imageFile.getInputStream(),
                imageFile.getOriginalFilename(),
                imageFile.getContentType(),
                imageFile.getSize(),
                "items/" + itemId + "/"
            );

            final String imageUrl = imageStoragePort.storeImage(fileWrapper);

            final Image imageDomain = new Image(imageUrl, itemId);
            final Image savedImageDomain = imageRepository.save(imageDomain);

            return imageMapper.toDto(savedImageDomain);

        } catch (final IOException e) {
            log.error("Failed to process image file for item ID {}: {}", itemId, e.getMessage(), e);
            throw new RuntimeException("Failed to process image file: " + e.getMessage(), e);
        }
    }

    @Override
    public ItemDto addImagesToItem(final Long itemId, final List<MultipartFile> imageFiles) {
        itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("Article non trouvé avec l'ID: " + itemId));

        imageFiles.forEach(file -> {
            if (!file.isEmpty()) {
                try {
                    final ImageFileWrapper fileWrapper = new ImageFileWrapper(
                        file.getInputStream(),
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize(),
                        "items/" + itemId + "/"
                    );
                    final String imageUrl = imageStoragePort.storeImage(fileWrapper);
                    final Image imageDomain = new Image(imageUrl, itemId);
                    imageRepository.save(imageDomain);
                } catch (final IOException e) {
                    log.error("Failed to process one of the image files for item ID {}: {}", itemId, e.getMessage(), e);
                }
            }
        });

        final Item updatedItem = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("Article non trouvé après l'ajout d'images: " + itemId)); 

        return itemMapper.toDto(updatedItem);
    }

    @Override
    public void deleteItemImage(final Long itemId, final Long imageId) {
        if (!itemRepository.existsById(itemId)) {
             throw new NotFoundException("Article non trouvé avec l'ID: " + itemId);
        }
        final Image image = imageRepository.findByIdAndItemId(imageId, itemId)
            .orElseThrow(() -> new NotFoundException("Image avec ID " + imageId + " non trouvée pour l'article ID " + itemId));
        
        imageStoragePort.deleteImage(image.getUrl());
        imageRepository.deleteById(image.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageDto> getItemImages(final Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException("Article non trouvé avec l'ID: " + itemId);
        }
        return imageMapper.toDtoList(imageRepository.findByItemId(itemId));
    }
}
