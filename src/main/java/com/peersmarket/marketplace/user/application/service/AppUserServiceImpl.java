package com.peersmarket.marketplace.user.application.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.shared.model.Password;
import com.peersmarket.marketplace.user.application.dto.AppUserDto;
import com.peersmarket.marketplace.user.application.port.in.AppUserService;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;
import com.peersmarket.marketplace.user.domain.model.AppUser;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.AppUserMapper; // Ou un mapper d'application si vous en avez un séparé

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository; // À créer ou à remplacer par votre JpaRepository si vous l'utilisez directement ici
    private final AppUserMapper appUserMapper; // Assurez-vous que ce mapper est approprié pour cette couche
    private final PasswordEncoder passwordEncoder; // Injection du PasswordEncoder

    @Override
    public AppUserDto createUser(final AppUserDto userDto) {
        final AppUser appUser = appUserMapper.toDomain(userDto);

        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            final String encodedPassword = passwordEncoder.encode(userDto.getPassword());
            appUser.setPassword(new Password(encodedPassword)); // Hachage du mot de passe
        } else {
            throw new IllegalArgumentException("Password cannot be null or empty for a new user.");
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
        return appUserRepository.findByUsername(username) // Assurez-vous que cette méthode existe dans votre port/repository
                .map(appUserMapper::toDto);
    }

    @Override
    public AppUserDto updateUser(final Long id, final AppUserDto userDto) {
        final AppUser existingUser = appUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id)); // Remplacez par une exception personnalisée

        // Mettez à jour les champs de existingUser avec les valeurs de userDto
        // Ceci est une mise à jour partielle, ajustez selon vos besoins
        final AppUser userToUpdate = appUserMapper.toDomain(userDto);
        
        // Il est important de ne pas écraser des champs non modifiables ou de gérer la logique de mise à jour avec soin
        existingUser.setUsername(userToUpdate.getUsername()); // Exemple
        existingUser.setBio(userToUpdate.getBio());
        existingUser.setAvatarUrl(userToUpdate.getAvatarUrl());
        existingUser.setAddress(userToUpdate.getAddress()); // Assurez-vous que le mapping d'adresse est correct
        // Ne mettez pas à jour le mot de passe ici, sauf si c'est une fonctionnalité spécifique
        // existingUser.setEmail(userToUpdate.getEmail()); // Soyez prudent avec la mise à jour de l'email

        final AppUser updatedUser = appUserRepository.save(existingUser);
        return appUserMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(final Long id) {
        if (!appUserRepository.existsById(id)) { // Assurez-vous que cette méthode existe
            throw new RuntimeException("User not found with id: " + id); // Remplacez par une exception personnalisée
        }
        appUserRepository.deleteById(id);
    }
}