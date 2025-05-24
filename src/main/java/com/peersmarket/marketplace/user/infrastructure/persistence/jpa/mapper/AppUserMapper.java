package com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.peersmarket.marketplace.shared.model.Email;
import com.peersmarket.marketplace.shared.model.Password;
import com.peersmarket.marketplace.user.application.dto.AppUserDto;
import com.peersmarket.marketplace.user.application.dto.CreateAddressDto;
import com.peersmarket.marketplace.user.application.dto.CreateUserDto;
import com.peersmarket.marketplace.user.domain.model.Address;
import com.peersmarket.marketplace.user.domain.model.AppUser;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model.AppUserEntity;

@Mapper(componentModel = "spring", uses = { AddressMapper.class })
public interface AppUserMapper {

    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "password.value", target = "password")
    AppUserEntity toEntity(AppUser domain);

    @Mapping(target = "email", qualifiedByName = "stringToEmail")
    @Mapping(target = "password", qualifiedByName = "stringToPassword")
    AppUser toDomain(AppUserEntity entity);

    @Mapping(source = "email.value", target = "email")
    AppUserDto toDto(AppUser domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", qualifiedByName = "stringToEmail")
    @Mapping(target = "password", source = "password", qualifiedByName = "stringToPassword")
    @Mapping(target = "joinDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "verified", ignore = true)
    @Mapping(source = "address", target = "address", qualifiedByName = "createAddressDtoToAddress")
    AppUser toDomain(CreateUserDto createUserDto);

    @Named("stringToEmail")
    default Email stringToEmail(final String email) {
        return email != null ? new Email(email) : null;
    }

    @Named("stringToPassword")
    default Password stringToPassword(final String password) {
        return password != null ? new Password(password) : null;
    }

    @Named("createAddressDtoToAddress")
    default Address createAddressDtoToAddress(final CreateAddressDto dto) {
        if (dto == null) {
            return null;
        }
        final Address address = new Address();
        address.setLongitude(dto.getLongitude());
        address.setLatitude(dto.getLatitude());
        return address;
    }
}