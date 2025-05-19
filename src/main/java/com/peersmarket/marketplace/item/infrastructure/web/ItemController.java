package com.peersmarket.marketplace.item.infrastructure.web;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.peersmarket.marketplace.item.application.dto.ImageDto;
import com.peersmarket.marketplace.item.application.dto.ItemDto;
import com.peersmarket.marketplace.item.application.port.in.ItemService;
import com.peersmarket.marketplace.shared.exception.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody final ItemDto itemDto) {
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

    @PostMapping("/{itemId}/images/single") // Renommé pour clarté vs batch
    public ResponseEntity<ImageDto> addItemImage(@PathVariable final Long itemId, @Valid @RequestBody final ImageDto imageDto) {
        try {
            final ImageDto newImage = itemService.addItemImage(itemId, imageDto);
            return new ResponseEntity<>(newImage, HttpStatus.CREATED);
        } catch (final NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @PostMapping("/{itemId}/images") // Endpoint pour ajouter plusieurs images
    public ResponseEntity<ItemDto> addImagesToItem(@PathVariable final Long itemId, @Valid @RequestBody final List<ImageDto> imageDtos) {
        try {
            final ItemDto updatedItem = itemService.addImagesToItem(itemId, imageDtos);
            return ResponseEntity.ok(updatedItem);
        } catch (final NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{itemId}/images/{imageId}")
    public ResponseEntity<Void> deleteItemImage(@PathVariable final Long itemId, @PathVariable final Long imageId) {
        try {
            itemService.deleteItemImage(itemId, imageId);
            return ResponseEntity.noContent().build();
        } catch (final NotFoundException e) {
            return ResponseEntity.notFound().build();
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
}