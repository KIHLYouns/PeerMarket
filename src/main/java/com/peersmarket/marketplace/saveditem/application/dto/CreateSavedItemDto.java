package com.peersmarket.marketplace.saveditem.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSavedItemDto {
    @NotNull(message = "L'ID de l'utilisateur ne peut pas être nul")
    private Long userId;

    @NotNull(message = "L'ID de l'article ne peut pas être nul")
    private Long itemId;
}
