package com.peersmarket.marketplace.saveditem.application.dto;

import java.time.LocalDateTime;

import com.peersmarket.marketplace.item.application.dto.ItemDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedItemDto {
    private Long id;

    @NotNull(message = "L'ID de l'utilisateur ne peut pas être nul")
    private Long userId;

    @NotNull(message = "L'ID de l'article ne peut pas être nul")
    private Long itemId;

    private LocalDateTime savedAt;

    // Optionnel: pour renvoyer les détails de l'item directement
    private ItemDto item;
}