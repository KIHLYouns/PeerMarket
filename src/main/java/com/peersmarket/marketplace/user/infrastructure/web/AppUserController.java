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
import com.peersmarket.marketplace.user.application.dto.CreateUserDto;
import com.peersmarket.marketplace.user.application.port.in.AppUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping
    public ResponseEntity<AppUserDto> createUser(@Valid @RequestBody final CreateUserDto createUserDto) {
        final AppUserDto createdUser = appUserService.createUser(createUserDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getUserById(@PathVariable final Long id) {
        final Optional<AppUserDto> userDto = appUserService.getUserById(id);
        return userDto.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AppUserDto> getUserByUsername(@PathVariable final String username) {
        final Optional<AppUserDto> userDto = appUserService.getUserByUsername(username);
        return userDto.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserDto> updateUser(@PathVariable final Long id, @Valid @RequestBody final AppUserDto userDto) {
        try {
            final AppUserDto updatedUser = appUserService.updateUser(id, userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (final RuntimeException e) { // Idéalement, une exception plus spécifique
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable final Long id) {
        try {
            appUserService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (final RuntimeException e) { // Idéalement, une exception plus spécifique
            return ResponseEntity.notFound().build();
        }
    }
}