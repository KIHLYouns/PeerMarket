package com.peersmarket.marketplace.conversation.application.port.out;

import java.util.List;
import java.util.Optional;

import com.peersmarket.marketplace.conversation.domain.model.Conversation;

public interface ConversationRepository {
    Conversation save(Conversation conversation);
    Optional<Conversation> findById(Long id);
    List<Conversation> findByUserId(Long userId); // Utilisateur participant à la conversation
    Optional<Conversation> findByItemIdAndUserIds(Long itemId, Long userId1, Long userId2);
}
