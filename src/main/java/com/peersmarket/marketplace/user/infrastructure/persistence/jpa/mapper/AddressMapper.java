package com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.peersmarket.marketplace.user.application.dto.AddressDto;
import com.peersmarket.marketplace.user.application.dto.CreateAddressDto;
import com.peersmarket.marketplace.user.domain.model.Address;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model.AddressEntity;

@Mapper(componentModel = "spring", uses = { CityMapper.class })
public interface AddressMapper {

    Address toDomain(AddressEntity entity);

    AddressEntity toEntity(Address domain);

    AddressDto toDto(Address domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    Address toDomain(CreateAddressDto createAddressDto);
}
