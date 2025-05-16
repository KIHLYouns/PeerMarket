package com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper;

import org.mapstruct.Mapper;

import com.peersmarket.marketplace.item.application.dto.CategoryDto;
import com.peersmarket.marketplace.item.domain.model.Category;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.model.CategoryEntity;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    Category toDomain(CategoryEntity categoryEntity);
    CategoryEntity toEntity(Category category);

    CategoryDto entityToDto(CategoryEntity categoryEntity);
    CategoryEntity dtoToEntity(CategoryDto categoryDto);
}
