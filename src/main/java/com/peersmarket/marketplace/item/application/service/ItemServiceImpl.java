package com.peersmarket.marketplace.item.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.item.application.dto.ImageDto;
import com.peersmarket.marketplace.item.application.dto.ItemDto;
import com.peersmarket.marketplace.item.application.port.in.ItemService;
import com.peersmarket.marketplace.item.application.port.out.CategoryRepository;
import com.peersmarket.marketplace.item.application.port.out.ImageRepository;
import com.peersmarket.marketplace.item.application.port.out.ItemRepository;
import com.peersmarket.marketplace.item.domain.model.Category;
import com.peersmarket.marketplace.item.domain.model.Image;
import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.item.domain.model.ItemStatus;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.ImageMapper;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.ItemMapper;
import com.peersmarket.marketplace.shared.exception.NotFoundException;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;
import com.peersmarket.marketplace.user.domain.model.AppUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ItemMapper itemMapper;
    private final ImageMapper imageMapper;

    @Override
    public ItemDto createItem(final ItemDto itemDto) {
        final AppUser seller = appUserRepository.findById(itemDto.getSellerId())
                .orElseThrow(() -> new NotFoundException("Vendeur non trouvé avec l'ID : " + itemDto.getSellerId()));
        final Category category = categoryRepository.findById(itemDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Catégorie non trouvée avec l'ID : " + itemDto.getCategoryId()));

        final Item item = itemMapper.toDomain(itemDto);
        item.setSeller(seller);
        item.setCategory(category);
        if (item.getStatus() == null) {
            item.setStatus(ItemStatus.AVAILABLE);
        }

        final Item savedDomainItem = itemRepository.saveAndFlush(item);

        final Item finalItemState = itemRepository.findById(savedDomainItem.getId())
            .orElseThrow(() -> new NotFoundException("Erreur lors de la récupération de l'item créé : " + savedDomainItem.getId()));
        
        return itemMapper.toDto(finalItemState);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemDto> getItemById(final Long id) {
        return itemRepository.findById(id).map(item -> {
            return itemMapper.toDto(item);
        });
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
            itemRepository.deleteById(item.getId());
    }

    @Override
    public ImageDto addItemImage(final Long itemId, final ImageDto imageDto) {
        final Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Article non trouvé avec l'ID: " + itemId));

        final Image image = imageMapper.toDomain(imageDto);
        image.setItemId(item.getId());

        final Image savedImage = imageRepository.save(image);
        return imageMapper.toDto(savedImage);
    }

    @Override
    public void deleteItemImage(final Long itemId, final Long imageId) {
        if (!itemRepository.existsById(itemId)) {
             throw new NotFoundException("Article non trouvé avec l'ID: " + itemId);
        }
        final Image image = imageRepository.findByIdAndItemId(imageId, itemId)
            .orElseThrow(() -> new NotFoundException("Image avec ID " + imageId + " non trouvée pour l'article ID " + itemId));
        
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

    @Override
    public ItemDto addImagesToItem(final Long itemId, final List<ImageDto> imageDtos) {
        final Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("Article non trouvé avec l'ID: " + itemId));

        final List<Image> newImages = new ArrayList<>();
        for (final ImageDto imageDto : imageDtos) {
            final Image image = imageMapper.toDomain(imageDto);
            image.setItemId(item.getId());
            newImages.add(imageRepository.save(image));
        }

        final Item updatedItem = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("Article non trouvé après ajout d'images: " + itemId));
        return itemMapper.toDto(updatedItem);
    }
}
