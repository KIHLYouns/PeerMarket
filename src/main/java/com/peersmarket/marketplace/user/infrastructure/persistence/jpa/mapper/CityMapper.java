package com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper;

import org.mapstruct.Mapper;

import com.peersmarket.marketplace.user.application.dto.CityDto;
import com.peersmarket.marketplace.user.domain.model.City;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model.CityEntity;

@Mapper(componentModel = "spring")
public interface CityMapper {        
    City toDomain(CityEntity entity);
    CityEntity toEntity(City domain);
    CityDto toDto(City domain);
    City toDomain(CityDto dto);
}
