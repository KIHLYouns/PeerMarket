package com.peersmarket.marketplace.item.application.service;

import com.peersmarket.marketplace.item.application.dto.ItemDto;
import com.peersmarket.marketplace.item.application.port.in.ItemService;
import com.peersmarket.marketplace.item.application.port.out.CategoryRepository;
import com.peersmarket.marketplace.item.application.port.out.ItemRepository;
import com.peersmarket.marketplace.item.domain.model.Category;
import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.item.domain.model.ItemStatus;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.ItemMapper;
import com.peersmarket.marketplace.shared.exception.NotFoundException;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;
import com.peersmarket.marketplace.user.domain.model.AppUser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto createItem(final ItemDto itemDto) {
        final AppUser seller = appUserRepository.findById(itemDto.getSellerId())
                .orElseThrow(() -> new NotFoundException("Vendeur non trouvé avec l'ID : " + itemDto.getSellerId()));
        final Category category = categoryRepository.findById(itemDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Catégorie non trouvée avec l'ID : " + itemDto.getCategoryId()));

        final Item item = itemMapper.toDomain(itemDto);
        item.setSeller(seller);
        item.setCategory(category);
        item.setCreatedAt(LocalDateTime.now());
        if (item.getStatus() == null) {
            item.setStatus(ItemStatus.AVAILABLE);
        }

        final Item savedItem = itemRepository.save(item);
        return itemMapper.toDto(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemDto> getItemById(final Long id) {
        return itemRepository.findById(id).map(itemMapper::toDto);
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

        final Item updatedItem = itemRepository.save(existingItem);
        return itemMapper.toDto(updatedItem);
    }

    @Override
    public void deleteItem(final Long id) {
        if (!itemRepository.existsById(id)) {
            throw new NotFoundException("Article non trouvé avec l'ID : " + id);
        }
        itemRepository.deleteById(id);
    }
}
