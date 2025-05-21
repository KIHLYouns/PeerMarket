package com.peersmarket.marketplace.saveditem.infrastructure.persistence.jpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.ItemMapper;
import com.peersmarket.marketplace.saveditem.application.dto.SavedItemDto;
import com.peersmarket.marketplace.saveditem.domain.model.SavedItem;
import com.peersmarket.marketplace.saveditem.infrastructure.persistence.jpa.model.SavedItemEntity;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.AppUserMapper;

@Mapper(componentModel = "spring", uses = {AppUserMapper.class, ItemMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SavedItemMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item", target = "item") // Pour inclure les détails de l'item dans le DTO
    SavedItemDto toDto(SavedItem savedItem);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "itemId", target = "item.id")
    SavedItem toDomain(SavedItemDto savedItemDto);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "item", target = "item")
    SavedItemEntity toEntity(SavedItem savedItem);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "item", target = "item")
    SavedItem toDomain(SavedItemEntity savedItemEntity);

    List<SavedItemDto> toDtoList(List<SavedItem> savedItems);
}
