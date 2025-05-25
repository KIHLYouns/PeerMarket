package com.peersmarket.marketplace.user.infrastructure.web;

import org.springframework.http.ResponseEntity;
// Importez vos annotations de sécurité si nécessaire (ex: @PreAuthorize, @AuthenticationPrincipal)
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peersmarket.marketplace.user.application.dto.UserProfileViewDto;
import com.peersmarket.marketplace.user.application.facade.UserProfileFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileFacade userProfileFacade;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileViewDto> getUserProfile(@PathVariable final Long userId) {
        return userProfileFacade.getUserProfile(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
