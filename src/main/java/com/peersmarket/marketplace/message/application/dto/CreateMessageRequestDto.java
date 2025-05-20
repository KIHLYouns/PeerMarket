package com.peersmarket.marketplace.message.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMessageRequestDto {
    @NotBlank(message = "Le contenu du message ne peut pas être vide.")
    @Size(min = 1, max = 1000, message = "Le message doit contenir entre 1 et 1000 caractères.")
    private String content;

    @NotNull(message = "L'ID de l'expéditeur ne peut pas être nul.")
    private Long senderId; // Sera typiquement l'utilisateur authentifié

    @NotNull(message = "L'ID de la conversation ne peut pas être nul.")
    private Long conversationId;
}
