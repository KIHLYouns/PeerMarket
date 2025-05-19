package com.peersmarket.marketplace.item.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private Long id;

    @NotBlank(message = "L'URL de l'image ne peut pas être vide")
    @Size(max = 2048, message = "L'URL de l'image ne peut pas dépasser 2048 caractères")
    private String url;

    private Long itemId;
}
