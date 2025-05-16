package com.peersmarket.marketplace.user.infrastructure.web;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peersmarket.marketplace.user.application.dto.AppUserDto;
import com.peersmarket.marketplace.user.application.port.in.AppUserService;

import jakarta.validation.Valid; // Pour la validation si vous ajoutez des annotations de validation au DTO
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping
    public ResponseEntity<AppUserDto> createUser(@Valid @RequestBody AppUserDto userDto) {
        // Le mot de passe ne devrait pas être dans le AppUserDto pour la création,
        // ou alors il faut un DTO spécifique pour la création (CreateUserRequestDto)
        // qui contiendrait le mot de passe en clair.
        // Pour l'instant, on suppose que AppUserDto est utilisé et que le service gère le mot de passe.
        AppUserDto createdUser = appUserService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getUserById(@PathVariable Long id) {
        Optional<AppUserDto> userDto = appUserService.getUserById(id);
        return userDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AppUserDto> getUserByUsername(@PathVariable String username) {
        Optional<AppUserDto> userDto = appUserService.getUserByUsername(username);
        return userDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserDto> updateUser(@PathVariable Long id, @Valid @RequestBody AppUserDto userDto) {
        // Assurez-vous que l'ID dans le DTO correspond à l'ID dans le chemin, ou ignorez l'ID du DTO.
        // La logique de mise à jour dans le service doit gérer quels champs peuvent être modifiés.
        try {
            AppUserDto updatedUser = appUserService.updateUser(id, userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) { // Remplacez par une exception plus spécifique si le service la lance
            // Par exemple, UserNotFoundException
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            appUserService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) { // Remplacez par une exception plus spécifique
            return ResponseEntity.notFound().build();
        }
    }
}