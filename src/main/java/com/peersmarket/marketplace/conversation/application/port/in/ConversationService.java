package com.peersmarket.marketplace.conversation.application.port.in;

import java.util.List;
import java.util.Optional;

import com.peersmarket.marketplace.conversation.application.dto.ConversationDto;
import com.peersmarket.marketplace.conversation.application.dto.CreateConversationRequestDto;
import com.peersmarket.marketplace.message.application.dto.MessageDto;

public interface ConversationService {
    ConversationDto createConversation(CreateConversationRequestDto createConversationDto);
    Optional<ConversationDto> getConversationById(Long conversationId);
    List<ConversationDto> getConversationsForUser(Long userId); // Basé sur UserConversation
    List<MessageDto> getMessagesForConversation(Long conversationId, int page, int size);
    // On pourrait aussi avoir une méthode pour trouver une conversation existante entre deux utilisateurs pour un item donné
    Optional<ConversationDto> findConversationByItemAndUsers(Long itemId, Long userId1, Long userId2);
}
