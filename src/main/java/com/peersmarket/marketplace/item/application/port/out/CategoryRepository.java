package com.peersmarket.marketplace.item.application.port.out;

import java.util.List;
import java.util.Optional;

import com.peersmarket.marketplace.item.domain.model.Category;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    Optional<Category> findByNameIgnoreCase(String name);
    List<Category> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
