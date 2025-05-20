package com.peersmarket.marketplace.conversation.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDto {
    private Long id;
    private Long itemId;
    private String itemTitle; // Pour affichage rapide
    private List<Long> participantIds;
    private List<String> participantUsernames; // Pour affichage rapide
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String lastMessageContent;
    private LocalDateTime lastMessageTimestamp;
    private Long lastMessageSenderId;
    private String lastMessageSenderUsername;
}
