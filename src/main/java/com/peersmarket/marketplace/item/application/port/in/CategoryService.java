package com.peersmarket.marketplace.item.application.port.in;

import java.util.List;
import java.util.Optional;

import com.peersmarket.marketplace.item.application.dto.CategoryDto;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    Optional<CategoryDto> getCategoryById(Long id);
    Optional<CategoryDto> getCategoryByName(String name);
    List<CategoryDto> getAllCategories();
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long id);
}
