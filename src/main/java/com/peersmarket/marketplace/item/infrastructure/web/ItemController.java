package com.peersmarket.marketplace.item.infrastructure.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.peersmarket.marketplace.item.application.dto.CreateItemDto;
import com.peersmarket.marketplace.item.application.dto.ImageDto;
import com.peersmarket.marketplace.item.application.dto.ItemDto;
import com.peersmarket.marketplace.item.application.port.in.ItemService;
import com.peersmarket.marketplace.item.application.port.in.ItemSuggestionService;
import com.peersmarket.marketplace.shared.exception.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemSuggestionService itemSuggestionService;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody final CreateItemDto itemDto) {
        // L'authentification/autorisation (ex: sellerId correspond à l'utilisateur connecté)
        // serait gérée ici ou dans le service avec Spring Security.
        final ItemDto createdItem = itemService.createItem(itemDto);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable final Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems(
            @RequestParam(required = false) final Long sellerId,
            @RequestParam(required = false) final Long categoryId,
            @RequestParam(required = false) final String title) {

        List<ItemDto> items;
        if (sellerId != null) {
            items = itemService.getItemsBySellerId(sellerId);
        } else if (categoryId != null) {
            items = itemService.getItemsByCategoryId(categoryId);
        } else if (title != null && !title.trim().isEmpty()) {
            items = itemService.searchItemsByTitle(title);
        } else {
            items = itemService.getAllItems();
        }
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable final Long id, @Valid @RequestBody final ItemDto itemDto) {
        // Ajouter la logique d'autorisation (ex: seul le vendeur peut modifier)
        try {
            final ItemDto updatedItem = itemService.updateItem(id, itemDto);
            return ResponseEntity.ok(updatedItem);
        } catch (final NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Ou un DTO d'erreur
        }
        // Gérer d'autres exceptions (validation, permissions)
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable final Long id) {
         // Ajouter la logique d'autorisation
        try {
            itemService.deleteItem(id);
            return ResponseEntity.noContent().build();
        } catch (final NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Endpoints pour la gestion des Images ---

    @PostMapping(value = "/{itemId}/images/single", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageDto> addSingleImageToItem(
            @PathVariable final Long itemId,
            @RequestParam("imageFile") final MultipartFile imageFile) {
        try {
            if (imageFile.isEmpty()) {
                return ResponseEntity.badRequest().build(); // Ou un message d'erreur plus spécifique
            }
            final ImageDto newImage = itemService.addImageToItem(itemId, imageFile);
            return new ResponseEntity<>(newImage, HttpStatus.CREATED);
        } catch (final NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (final RuntimeException e) { // Pour les erreurs de stockage ou de traitement de fichier
            log.error("Error uploading single image for item {}: {}", itemId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping(value = "/{itemId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemDto> addMultipleImagesToItem(
            @PathVariable final Long itemId,
            @RequestParam("imageFiles") final List<MultipartFile> imageFiles) {
        try {
            if (imageFiles.isEmpty() || imageFiles.stream().allMatch(MultipartFile::isEmpty)) {
                return ResponseEntity.badRequest().build(); // Ou un message d'erreur
            }
            final ItemDto updatedItem = itemService.addImagesToItem(itemId, imageFiles);
            return ResponseEntity.ok(updatedItem);
        } catch (final NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (final RuntimeException e) { // Pour les erreurs de stockage ou de traitement de fichier
            log.error("Error uploading multiple images for item {}: {}", itemId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{itemId}/images/{imageId}")
    public ResponseEntity<Void> deleteItemImage(@PathVariable final Long itemId, @PathVariable final Long imageId) {
        try {
            itemService.deleteItemImage(itemId, imageId);
            return ResponseEntity.noContent().build();
        } catch (final NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (final RuntimeException e) { // Pour les erreurs de suppression du stockage
            log.error("Error deleting image {} for item {}: {}", imageId, itemId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{itemId}/images")
    public ResponseEntity<List<ImageDto>> getItemImages(@PathVariable final Long itemId) {
        try {
            final List<ImageDto> images = itemService.getItemImages(itemId);
            return ResponseEntity.ok(images);
        } catch (final NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<ItemDto>> getItemSuggestions(
            @RequestParam(name = "strategy", required = false) final String strategyName) {
        List<ItemDto> suggestedItems;
        if (strategyName != null && !strategyName.trim().isEmpty()) {
            suggestedItems = itemSuggestionService.getSuggestedItems(strategyName);
        } else {
            suggestedItems = itemSuggestionService.getSuggestedItems();
        }
        return ResponseEntity.ok(suggestedItems);
    }
}