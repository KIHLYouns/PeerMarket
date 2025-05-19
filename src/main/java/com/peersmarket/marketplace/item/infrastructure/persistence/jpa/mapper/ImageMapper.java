package com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.peersmarket.marketplace.item.application.dto.ImageDto;
import com.peersmarket.marketplace.item.domain.model.Image;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.model.ImageEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper {

    ImageDto toDto(Image image);
    List<ImageDto> toDtoList(List<Image> images);

    Image toDomain(ImageDto imageDto);
    List<Image> toDomainListFromDto(List<ImageDto> imageDtos); // Renommée

    @Mapping(target = "item", ignore = true) // Ignorer le mappage de l'item ici
    ImageEntity toEntity(Image image);
    List<ImageEntity> toEntityList(List<Image> images);

    @Mapping(source = "item.id", target = "itemId")
    Image toDomain(ImageEntity imageEntity);
    List<Image> toDomainListFromEntity(List<ImageEntity> imageEntities); // Renommée
}