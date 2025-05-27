package com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.peersmarket.marketplace.item.application.dto.ItemDto;
import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.model.ItemEntity;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.AppUserMapper;

@Mapper(componentModel = "spring", uses = {AppUserMapper.class, CategoryMapper.class, ImageMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "images", target = "images")
    @Mapping(source = "seller.username", target = "sellerUsername")
    @Mapping(source = "seller.avatarUrl", target = "sellerAvatarUrl")
    @Mapping(source = "seller.averageRating", target = "sellerAverageRating")
    @Mapping(source = "seller.ratingCount", target = "sellerRatingCount")
    @Mapping(source = "seller.address.city.id", target = "sellerCityId")
    @Mapping(source = "seller.address.city.name", target = "sellerCityName")
    ItemDto toDto(Item item);

    @Mapping(source = "sellerId", target = "seller.id")
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "images", target = "images")
    Item toDomain(ItemDto itemDto);

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "seller", source = "seller")
    @Mapping(target = "category", source = "category")
    ItemEntity toEntity(Item item);

    @Mapping(target = "seller", source = "seller")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "images", source = "images")
    Item toDomain(ItemEntity itemEntity);
}
