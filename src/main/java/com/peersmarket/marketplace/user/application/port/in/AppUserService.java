package com.peersmarket.marketplace.user.application.port.in;

import java.util.Optional;

import com.peersmarket.marketplace.user.application.dto.AppUserDto;
import com.peersmarket.marketplace.user.application.dto.CreateUserDto;

public interface AppUserService {
    AppUserDto createUser(CreateUserDto createUserDto);
    Optional<AppUserDto> getUserById(Long id);
    Optional<AppUserDto> getUserByUsername(String username);
    AppUserDto updateUser(Long id, AppUserDto userDto);
    void deleteUser(Long id);
    // Ajoutez d'autres méthodes comme changePassword, verifyUser, etc.
}