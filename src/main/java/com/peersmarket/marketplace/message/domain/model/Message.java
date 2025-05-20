package com.peersmarket.marketplace.message.domain.model;

import java.time.LocalDateTime;

import com.peersmarket.marketplace.conversation.domain.model.Conversation;
import com.peersmarket.marketplace.user.domain.model.AppUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private AppUser sender;
    private Conversation conversation;
    private boolean isRead;

    // Logique métier potentielle :
    // - Marquer comme lu
}