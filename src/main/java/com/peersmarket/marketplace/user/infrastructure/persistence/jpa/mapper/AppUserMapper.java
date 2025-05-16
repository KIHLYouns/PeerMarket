package com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.peersmarket.marketplace.shared.model.Email;
import com.peersmarket.marketplace.shared.model.Password;
import com.peersmarket.marketplace.user.application.dto.AppUserDto;
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
    @Mapping(target = "password", ignore = true)
    AppUserDto toDto(AppUser domain);

    @Mapping(target = "email", qualifiedByName = "stringToEmail")
    @Mapping(target = "password", source = "password", qualifiedByName = "stringToPassword")
    AppUser toDomain(AppUserDto dto);

    @Named("stringToEmail")
    default Email stringToEmail(final String email) {
        return email == null ? null : new Email(email);
    }

    @Named("stringToPassword")
    default Password stringToPassword(final String password) {
        return password == null ? null : new Password(password);
    }
}