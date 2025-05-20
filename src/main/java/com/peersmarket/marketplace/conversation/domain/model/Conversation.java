package com.peersmarket.marketplace.conversation.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.user.domain.model.AppUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation {
    private Long id;
    private Item item; // L'article concerné par la conversation
    @Builder.Default
    private List<AppUser> participants = new ArrayList<>(); // Les utilisateurs participant à la conversation
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String lastMessageContent;
    private LocalDateTime lastMessageTimestamp;
    private AppUser lastMessageSender; // Qui a envoyé le dernier message

    // Logique métier potentielle :
    // - Ajouter un participant
    // - Mettre à jour les informations du dernier message
}
