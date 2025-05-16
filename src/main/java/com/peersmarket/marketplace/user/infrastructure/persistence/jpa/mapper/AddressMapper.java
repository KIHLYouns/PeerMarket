package com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper;

import org.mapstruct.Mapper;

import com.peersmarket.marketplace.user.application.dto.AddressDto;
import com.peersmarket.marketplace.user.domain.model.Address;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model.AddressEntity;

@Mapper(componentModel = "spring", uses = {CityMapper.class})
public interface AddressMapper {
        
    Address toDomain(AddressEntity entity);    
    AddressEntity toEntity(Address domain);

    AddressDto toDto(Address domain);
    Address toDomain(AddressDto dto);
}
