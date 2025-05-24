package com.peersmarket.marketplace.item.application.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.peersmarket.marketplace.item.domain.model.ItemCondition;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateItemDto {
    
    @NotBlank(message = "Le titre ne peut pas être vide")
    @Size(min = 3, max = 100, message = "Le titre doit contenir entre 3 et 100 caractères")
    private String title;
    
    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
    private String description;
    
    @NotNull(message = "Le prix ne peut pas être nul")
    @DecimalMin(value = "0.01", message = "Le prix doit être supérieur à 0")
    private BigDecimal price;
    
    @NotNull(message = "La condition ne peut pas être nulle")
    private ItemCondition condition;
    
    @NotNull(message = "L'ID du vendeur ne peut pas être nul")
    private Long sellerId;
    
    @NotNull(message = "L'ID de la catégorie ne peut pas être nul")
    private Long categoryId;
    
    private List<ImageDto> images = new ArrayList<>();
}