package com.peersmarket.marketplace.item.infrastructure.persistence;

import com.peersmarket.marketplace.item.application.port.out.ItemRepository;
import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.ItemMapper;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.model.ItemEntity;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.repository.ItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final ItemJpaRepository itemJpaRepository;
    private final ItemMapper itemMapper;

    @Override
    public Item save(Item item) {
        ItemEntity itemEntity = itemMapper.toEntity(item);
        ItemEntity savedEntity = itemJpaRepository.save(itemEntity);
        return itemMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemJpaRepository.findById(id).map(itemMapper::toDomain);
    }

    @Override
    public List<Item> findAll() {
        return itemJpaRepository.findAll().stream()
                .map(itemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        itemJpaRepository.deleteById(id);
    }

    @Override
    public List<Item> findBySellerId(Long sellerId) {
        return itemJpaRepository.findBySellerId(sellerId).stream()
                .map(itemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByCategoryId(Long categoryId) {
        return itemJpaRepository.findByCategoryId(categoryId).stream()
                .map(itemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByTitleContaining(String title) {
        // Utilise la méthode du JpaRepository qui ignore la casse
        return itemJpaRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(itemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return itemJpaRepository.existsById(id);
    }
}