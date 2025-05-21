package com.peersmarket.marketplace.saveditem.infrastructure.web;

import com.peersmarket.marketplace.saveditem.application.dto.CreateSavedItemDto;
import com.peersmarket.marketplace.saveditem.application.dto.SavedItemDto;
import com.peersmarket.marketplace.saveditem.application.port.in.SavedItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api") // Base path
@RequiredArgsConstructor
public class SavedItemController {

    private final SavedItemService savedItemService;

    // POST /api/saved-items (userId et itemId dans le corps)
    @PostMapping("/saved-items")
    public ResponseEntity<SavedItemDto> saveItem(@Valid @RequestBody final CreateSavedItemDto createSavedItemDto) {
        // Idéalement, userId devrait provenir de l'utilisateur authentifié (Spring Security)
        // Pour l'instant, on le prend du DTO.
        final SavedItemDto savedItem = savedItemService.saveItem(createSavedItemDto);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    // DELETE /api/users/{userId}/saved-items/{itemId}
    @DeleteMapping("/users/{userId}/saved-items/{itemId}")
    public ResponseEntity<Void> unsaveItem(@PathVariable final Long userId, @PathVariable final Long itemId) {
        // Idéalement, userId devrait être validé contre l'utilisateur authentifié.
        savedItemService.unsaveItem(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    // GET /api/users/{userId}/saved-items
    @GetMapping("/users/{userId}/saved-items")
    public ResponseEntity<List<SavedItemDto>> getSavedItemsByUser(@PathVariable final Long userId) {
        // Idéalement, userId devrait être validé contre l'utilisateur authentifié.
        final List<SavedItemDto> savedItems = savedItemService.getSavedItemsByUserId(userId);
        return ResponseEntity.ok(savedItems);
    }

    // GET /api/users/{userId}/saved-items/{itemId}/exists
    @GetMapping("/users/{userId}/saved-items/{itemId}/exists")
    public ResponseEntity<Map<String, Boolean>> isItemSavedByUser(@PathVariable final Long userId, @PathVariable final Long itemId) {
        // Idéalement, userId devrait être validé contre l'utilisateur authentifié.
        boolean isSaved = savedItemService.isItemSavedByUser(userId, itemId);
        return ResponseEntity.ok(Map.of("isSaved", isSaved));
    }
}