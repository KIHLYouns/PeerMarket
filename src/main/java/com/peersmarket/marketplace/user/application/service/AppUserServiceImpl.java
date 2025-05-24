package com.peersmarket.marketplace.user.application.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.shared.model.Password;
import com.peersmarket.marketplace.user.application.dto.AppUserDto;
import com.peersmarket.marketplace.user.application.dto.CreateAddressDto;
import com.peersmarket.marketplace.user.application.dto.CreateUserDto;
import com.peersmarket.marketplace.user.application.port.in.AddressService;
import com.peersmarket.marketplace.user.application.port.in.AppUserService;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;
import com.peersmarket.marketplace.user.domain.model.Address;
import com.peersmarket.marketplace.user.domain.model.AppUser;
import com.peersmarket.marketplace.user.domain.model.AppUserRole;
import com.peersmarket.marketplace.user.domain.model.UserStatus;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.AppUserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;

    @Override
    public AppUserDto createUser(final CreateUserDto createUserDto) {
        final AppUser appUser = appUserMapper.toDomain(createUserDto);

        if (createUserDto.getPassword() != null && !createUserDto.getPassword().isBlank()) {
            final String encodedPassword = passwordEncoder.encode(createUserDto.getPassword());
            appUser.setPassword(new Password(encodedPassword));
        } else {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être nul ou vide pour un nouvel utilisateur.");
        }

        appUser.setJoinDate(LocalDate.now());
        appUser.setStatus(UserStatus.ACTIVE);
        appUser.setRole(AppUserRole.USER);
        appUser.setVerified(false);

        if (createUserDto.getAddress() != null) {
            final CreateAddressDto createAddressDto = createUserDto.getAddress();
            final Address address = addressService.createAddressFromCoordinates(createAddressDto.getLongitude(), createAddressDto.getLatitude());
            appUser.setAddress(address);
        }

        final AppUser savedUser = appUserRepository.save(appUser);
        return appUserMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppUserDto> getUserById(final Long id) {
        return appUserRepository.findById(id)
                .map(appUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppUserDto> getUserByUsername(final String username) {
        return appUserRepository.findByUsername(username)
                .map(appUserMapper::toDto);
    }

    @Override
    public AppUserDto updateUser(final Long id, final AppUserDto userDto) {
        final AppUser existingUser = appUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec id: " + id));

        if (userDto.getUsername() != null) {
            existingUser.setUsername(userDto.getUsername());
        }
        if (userDto.getBio() != null) {
            existingUser.setBio(userDto.getBio());
        }
        if (userDto.getAvatarUrl() != null) {
            existingUser.setAvatarUrl(userDto.getAvatarUrl());
        }
        // La mise à jour de l'adresse peut nécessiter une logique plus complexe.
        // Si userDto.getAddress() n'est pas null, vous pourriez vouloir mettre à jour l'adresse.
        // Cela pourrait impliquer de récupérer l'adresse existante, de la mettre à jour ou d'en créer une nouvelle.
        if (userDto.getAddress() != null && userDto.getAddress().getLongitude() != null && userDto.getAddress().getLatitude() != null) {
            final Address updatedAddress = addressService.createAddressFromCoordinates(
                userDto.getAddress().getLongitude(),
                userDto.getAddress().getLatitude()
            );
            existingUser.setAddress(updatedAddress);
        }

        final AppUser updatedUser = appUserRepository.save(existingUser);
        return appUserMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(final Long id) {
        if (!appUserRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec id: " + id);
        }
        appUserRepository.deleteById(id);
    }
}