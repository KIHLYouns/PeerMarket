package com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.peersmarket.marketplace.item.application.dto.ItemDto;
import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.model.ItemEntity;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.AppUserMapper;

@Mapper(componentModel = "spring", uses = {AppUserMapper.class, CategoryMapper.class})
public interface ItemMapper {

    @Mappings({
        @Mapping(source = "seller.id", target = "sellerId"),
        @Mapping(source = "category.id", target = "categoryId"),
        @Mapping(source = "seller", target = "sellerInfo"),
        @Mapping(source = "category", target = "categoryInfo")
    })
    ItemDto toDto(Item item);

    @Mappings({
        @Mapping(target = "seller", ignore = true),
        @Mapping(target = "category", ignore = true)
    })
    Item toDomain(ItemDto itemDto);

    ItemEntity toEntity(Item item);

    Item toDomain(ItemEntity itemEntity);
}
