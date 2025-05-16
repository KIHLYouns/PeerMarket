package com.peersmarket.marketplace.item.infrastructure.web;

import com.peersmarket.marketplace.item.application.dto.ItemDto;
import com.peersmarket.marketplace.item.application.port.in.ItemService;
import com.peersmarket.marketplace.shared.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto) {
        // L'authentification/autorisation (ex: sellerId correspond à l'utilisateur connecté)
        // serait gérée ici ou dans le service avec Spring Security.
        ItemDto createdItem = itemService.createItem(itemDto);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems(
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String title) {

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
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDto itemDto) {
        // Ajouter la logique d'autorisation (ex: seul le vendeur peut modifier)
        try {
            ItemDto updatedItem = itemService.updateItem(id, itemDto);
            return ResponseEntity.ok(updatedItem);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        // Gérer d'autres exceptions (validation, permissions)
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
         // Ajouter la logique d'autorisation
        try {
            itemService.deleteItem(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}