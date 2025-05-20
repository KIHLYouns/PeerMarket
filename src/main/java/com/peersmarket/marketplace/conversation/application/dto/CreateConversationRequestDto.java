package com.peersmarket.marketplace.conversation.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateConversationRequestDto {
    @NotNull(message = "L'ID de l'article ne peut pas être nul.")
    private Long itemId;

    @NotNull(message = "L'ID de l'initiateur ne peut pas être nul.")
    private Long initiatorId; // L'utilisateur qui démarre la conversation (souvent l'acheteur potentiel)

    // Le premier message est souvent inclus lors de la création d'une conversation
    @NotNull(message = "Le contenu du premier message ne peut pas être nul.")
    @Size(min = 1, max = 1000, message = "Le message doit contenir entre 1 et 1000 caractères.")
    private String initialMessageContent;
}
