package com.peersmarket.marketplace.item.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.peersmarket.marketplace.item.application.port.out.CategoryRepository;
import com.peersmarket.marketplace.item.domain.model.Category;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.CategoryMapper;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.model.CategoryEntity;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.repository.CategoryJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category save(final Category category) {
        final CategoryEntity categoryEntity = categoryMapper.toEntity(category);
        final CategoryEntity savedEntity = categoryJpaRepository.save(categoryEntity);
        return categoryMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Category> findById(final Long id) {
        return categoryJpaRepository.findById(id).map(categoryMapper::toDomain);
    }

    @Override
    public Optional<Category> findByNameIgnoreCase(final String name) {
        return categoryJpaRepository.findByNameIgnoreCase(name).map(categoryMapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll().stream()
                .map(categoryMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(final Long id) {
        categoryJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(final Long id) {
        return categoryJpaRepository.existsById(id);
    }
}
