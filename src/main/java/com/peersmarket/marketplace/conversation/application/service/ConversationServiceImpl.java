package com.peersmarket.marketplace.conversation.application.service;

import com.peersmarket.marketplace.conversation.application.dto.ConversationDto;
import com.peersmarket.marketplace.conversation.application.dto.CreateConversationRequestDto;
import com.peersmarket.marketplace.conversation.application.port.in.ConversationService;
import com.peersmarket.marketplace.conversation.application.port.out.ConversationRepository;
import com.peersmarket.marketplace.conversation.domain.model.Conversation;
import com.peersmarket.marketplace.item.application.port.out.ItemRepository; // Assurez-vous que ce port existe
import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.message.application.dto.CreateMessageRequestDto;
import com.peersmarket.marketplace.message.application.dto.MessageDto;
import com.peersmarket.marketplace.message.application.port.in.MessageService;
import com.peersmarket.marketplace.message.application.port.out.MessageRepository;
import com.peersmarket.marketplace.shared.exception.NotFoundException;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository; // Assurez-vous que ce port existe
import com.peersmarket.marketplace.user.domain.model.AppUser;
import com.peersmarket.marketplace.conversation.infrastructure.persistence.jpa.mapper.ConversationMapper; // À créer
import com.peersmarket.marketplace.message.infrastructure.persistence.jpa.mapper.MessageMapper; // Créé ci-dessus
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final AppUserRepository appUserRepository;
    private final ItemRepository itemRepository;
    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;
    private final MessageService messageService; // Pour déléguer la création du message

    @Override
    public ConversationDto createConversation(CreateConversationRequestDto createDto) {
        AppUser initiator = appUserRepository.findById(createDto.getInitiatorId())
                .orElseThrow(() -> new NotFoundException("Initiator user not found with id: " + createDto.getInitiatorId()));
        Item item = itemRepository.findById(createDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + createDto.getItemId()));
        
        // Le vendeur est l'autre participant
        AppUser seller = item.getSeller();
        if (seller == null) {
             throw new NotFoundException("Seller not found for item id: " + item.getId());
        }

        if (initiator.getId().equals(seller.getId())) {
            throw new IllegalArgumentException("Cannot start a conversation with yourself for your own item.");
        }

        Optional<Conversation> existingConversationOpt = conversationRepository.findByItemIdAndUserIds(item.getId(), initiator.getId(), seller.getId());

        if (existingConversationOpt.isPresent()) {
            Conversation existingConversation = existingConversationOpt.get();
            // Envoyer le message initial à la conversation existante
            messageService.sendMessage(new CreateMessageRequestDto(
                createDto.getInitialMessageContent(),
                initiator.getId(),
                existingConversation.getId()
            ));
            // Recharger pour avoir les dernières infos
            Conversation updatedExistingConv = conversationRepository.findById(existingConversation.getId())
                .orElseThrow(() -> new NotFoundException("Existing conversation not found after sending message, id: " + existingConversation.getId()));
            return conversationMapper.toDto(updatedExistingConv);
        }

        Conversation conversation = Conversation.builder()
                .item(item)
                .participants(Arrays.asList(initiator, seller))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Conversation savedConversation = conversationRepository.save(conversation);

        // Créer le premier message
        messageService.sendMessage(new CreateMessageRequestDto(
                createDto.getInitialMessageContent(),
                initiator.getId(),
                savedConversation.getId()
        ));
        
        // Recharger la conversation pour avoir les infos du dernier message mises à jour par MessageService
        Conversation finalConversation = conversationRepository.findById(savedConversation.getId())
                                            .orElseThrow(() -> new NotFoundException("Conversation disappeared after creation, id: " + savedConversation.getId()));

        return conversationMapper.toDto(finalConversation);
    }

    @Override
    public Optional<ConversationDto> getConversationById(Long conversationId) {
        return conversationRepository.findById(conversationId).map(conversationMapper::toDto);
    }

    @Override
    public List<ConversationDto> getConversationsForUser(Long userId) {
        appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        return conversationRepository.findByUserId(userId).stream()
                .map(conversationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageDto> getMessagesForConversation(Long conversationId, int page, int size) {
        conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation not found with id: " + conversationId));
        Pageable pageable = PageRequest.of(page, size);
        return messageRepository.findByConversationIdOrderByTimestampDesc(conversationId, pageable)
                .getContent().stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<ConversationDto> findConversationByItemAndUsers(Long itemId, Long userId1, Long userId2) {
        return conversationRepository.findByItemIdAndUserIds(itemId, userId1, userId2)
                .map(conversationMapper::toDto);
    }
}