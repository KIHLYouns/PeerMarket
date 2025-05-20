package com.peersmarket.marketplace.message.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.conversation.application.port.out.ConversationRepository;
import com.peersmarket.marketplace.conversation.domain.model.Conversation;
import com.peersmarket.marketplace.message.application.dto.CreateMessageRequestDto;
import com.peersmarket.marketplace.message.application.dto.MessageDto;
import com.peersmarket.marketplace.message.application.port.in.MessageService;
import com.peersmarket.marketplace.message.application.port.out.MessageRepository;
import com.peersmarket.marketplace.message.domain.model.Message;
import com.peersmarket.marketplace.message.infrastructure.persistence.jpa.mapper.MessageMapper;
import com.peersmarket.marketplace.shared.exception.ForbiddenException;
import com.peersmarket.marketplace.shared.exception.NotFoundException;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;
import com.peersmarket.marketplace.user.domain.model.AppUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final AppUserRepository appUserRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageDto sendMessage(final CreateMessageRequestDto createDto) {
        final AppUser sender = appUserRepository.findById(createDto.getSenderId())
                .orElseThrow(() -> new NotFoundException("Sender user not found with id: " + createDto.getSenderId()));

        final Conversation conversation = conversationRepository.findById(createDto.getConversationId())
                .orElseThrow(() -> new NotFoundException("Conversation not found with id: " + createDto.getConversationId()));

        final boolean isParticipant = conversation.getParticipants().stream()
                                    .anyMatch(participant -> participant.getId().equals(sender.getId()));
        if (!isParticipant) {
            throw new ForbiddenException("Sender is not a participant in this conversation.");
        }

        final Message message = Message.builder()
                .content(createDto.getContent())
                .sender(sender)
                .conversation(conversation)
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        final Message savedMessage = messageRepository.save(message);

        // Mettre à jour la conversation avec les informations du dernier message
        conversation.setLastMessageContent(savedMessage.getContent());
        conversation.setLastMessageSender(savedMessage.getSender());
        conversation.setLastMessageTimestamp(savedMessage.getTimestamp());
        conversation.setUpdatedAt(savedMessage.getTimestamp());
        conversationRepository.save(conversation);

        return messageMapper.toDto(savedMessage);
    }

    @Override
    public List<MessageDto> markAllMessagesAsRead(final Long conversationId, final Long readerId) {
        final Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation not found with id: " + conversationId));

        // Vérifier si readerId est un participant de la conversation
        final boolean isParticipant = conversation.getParticipants().stream()
                .anyMatch(participant -> participant.getId().equals(readerId));
        if (!isParticipant) {
            throw new ForbiddenException("User " + readerId + " is not a participant of conversation " + conversationId);
        }
        
        appUserRepository.findById(readerId)
                .orElseThrow(() -> new NotFoundException("Reader user not found with id: " + readerId));


        final List<Message> messagesInConversation = messageRepository.findAllByConversationId(conversationId);

        final List<Message> messagesToUpdate = messagesInConversation.stream()
                .filter(msg -> !msg.getSender().getId().equals(readerId) && !msg.isRead())
                .peek(msg -> {
                    msg.setRead(true);
                })
                .collect(Collectors.toList());

        if (!messagesToUpdate.isEmpty()) {
            messageRepository.saveAll(messagesToUpdate);
        }

        return messagesInConversation.stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }
}
