package com.peersmarket.marketplace.item.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.item.application.port.out.ItemRepository;
import com.peersmarket.marketplace.item.domain.model.Image;
import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.ImageMapper;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.ItemMapper;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.model.ImageEntity;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.model.ItemEntity;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.repository.ItemJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final ItemJpaRepository itemJpaRepository;
    private final ItemMapper itemMapper;
    private final ImageMapper imageMapper;

    @Override
    @Transactional
    public Item save(final Item itemDomain) {
        final ItemEntity itemEntity = itemMapper.toEntity(itemDomain);

        if (itemDomain.getImages() != null && !itemDomain.getImages().isEmpty()) {
            itemEntity.getImages().clear();
            for (final Image domainImage : itemDomain.getImages()) {
                final ImageEntity imageEntityChild = imageMapper.toEntity(domainImage);
                itemEntity.addImage(imageEntityChild);
            }
        }
        final ItemEntity savedEntity = itemJpaRepository.save(itemEntity);
        return itemMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public Item saveAndFlush(final Item itemDomain) {
        final ItemEntity itemEntity = itemMapper.toEntity(itemDomain);

        if (itemDomain.getImages() != null && !itemDomain.getImages().isEmpty()) {
            itemEntity.getImages().clear();
            for (final Image domainImage : itemDomain.getImages()) {
                final ImageEntity imageEntityChild = imageMapper.toEntity(domainImage);
                itemEntity.addImage(imageEntityChild);
            }
        }
        final ItemEntity savedEntity = itemJpaRepository.saveAndFlush(itemEntity);
        return itemMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Item> findById(final Long id) {
        return itemJpaRepository.findById(id).map(itemMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> findAll() {
        return itemJpaRepository.findAll().stream()
                .map(itemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(final Long id) {
        itemJpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(final Long id) {
        return itemJpaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> findBySellerId(final Long sellerId) {
        return itemJpaRepository.findBySellerId(sellerId).stream()
                .map(itemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> findByCategoryId(final Long categoryId) {
        return itemJpaRepository.findByCategoryId(categoryId).stream()
                .map(itemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> findByTitleContaining(final String title) {
        return itemJpaRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(itemMapper::toDomain)
                .collect(Collectors.toList());
    }
}