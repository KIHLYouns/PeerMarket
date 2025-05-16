package com.peersmarket.marketplace.item.application.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.item.application.dto.CategoryDto;
import com.peersmarket.marketplace.item.application.port.in.CategoryService;
import com.peersmarket.marketplace.item.application.port.out.CategoryRepository;
import com.peersmarket.marketplace.item.domain.model.Category;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.CategoryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(final CategoryDto categoryDto) {
        final Category category = new Category(categoryDto.getName());
        final Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public Optional<CategoryDto> getCategoryById(final Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDto);
    }

    @Override
    public Optional<CategoryDto> getCategoryByName(final String name) {
        return categoryRepository.findByNameIgnoreCase(name).map(categoryMapper::toDto);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(final Long id, final CategoryDto categoryDto) {
        final Category categoryToUpdate = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        categoryToUpdate.setName(categoryDto.getName());
        final Category updatedCategory = categoryRepository.save(categoryToUpdate);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public void deleteCategory(final Long id) {
        if (!categoryRepository.findById(id).isPresent()) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
