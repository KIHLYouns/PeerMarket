package com.peersmarket.marketplace.message.application.port.in;

import java.util.List;

import com.peersmarket.marketplace.message.application.dto.CreateMessageRequestDto;
import com.peersmarket.marketplace.message.application.dto.MessageDto;

public interface MessageService {
    MessageDto sendMessage(CreateMessageRequestDto createMessageDto);
    List<MessageDto> markAllMessagesAsRead(Long conversationId, Long readerId);
}