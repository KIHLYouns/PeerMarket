package com.peersmarket.marketplace.conversation.infrastructure.persistence;

import com.peersmarket.marketplace.conversation.application.port.out.ConversationRepository;
import com.peersmarket.marketplace.conversation.domain.model.Conversation;
import com.peersmarket.marketplace.conversation.infrastructure.persistence.jpa.mapper.ConversationMapper;
import com.peersmarket.marketplace.conversation.infrastructure.persistence.jpa.model.ConversationEntity;
import com.peersmarket.marketplace.conversation.infrastructure.persistence.jpa.repository.ConversationJpaRepository;
import com.peersmarket.marketplace.item.application.port.out.ItemRepository;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.ItemMapper;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.AppUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConversationRepositoryImpl implements ConversationRepository {

    private final ConversationJpaRepository conversationJpaRepository;
    private final ConversationMapper conversationMapper;
    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;


    @Override
    @Transactional
    public Conversation save(Conversation conversation) {
        ConversationEntity conversationEntity = conversationMapper.toEntity(conversation);

        if (conversation.getItem() != null && conversation.getItem().getId() != null) {
            itemRepository.findById(conversation.getItem().getId())
                .map(itemMapper::toEntity)
                .ifPresent(conversationEntity::setItem);
        } else if (conversation.getItem() != null) { // Cas où l'item est nouveau et n'a pas d'ID (moins probable ici)
             // Logique pour sauvegarder un nouvel item si nécessaire, ou lancer une erreur
        }


        if (conversation.getParticipants() != null && !conversation.getParticipants().isEmpty()) {
            List<com.peersmarket.marketplace.user.domain.model.AppUser> participantsDomain = conversation.getParticipants().stream()
                .map(appUser -> appUserRepository.findById(appUser.getId()).orElse(null))
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
            conversationEntity.setParticipants(participantsDomain.stream().map(appUserMapper::toEntity).collect(Collectors.toList()));
        }
        
        if (conversation.getLastMessageSender() != null && conversation.getLastMessageSender().getId() != null) {
             appUserRepository.findById(conversation.getLastMessageSender().getId())
                .map(appUserMapper::toEntity)
                .ifPresent(conversationEntity::setLastMessageSender);
        }

        ConversationEntity savedEntity = conversationJpaRepository.save(conversationEntity);
        return conversationMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Conversation> findById(Long id) {
        return conversationJpaRepository.findById(id).map(conversationMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Conversation> findByUserId(Long userId) {
        return conversationJpaRepository.findByParticipantId(userId).stream()
                .map(conversationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Conversation> findByItemIdAndUserIds(Long itemId, Long userId1, Long userId2) {
        // Tente de trouver avec (userId1, userId2)
        Optional<ConversationEntity> conversationEntityOpt = conversationJpaRepository.findByItemIdAndExactParticipants(itemId, userId1, userId2);
        if (conversationEntityOpt.isPresent()) {
            return conversationEntityOpt.map(conversationMapper::toDomain);
        }
        // Si non trouvé, tente avec (userId2, userId1) car l'ordre des participants ne devrait pas importer
        return conversationJpaRepository.findByItemIdAndExactParticipants(itemId, userId2, userId1)
                                      .map(conversationMapper::toDomain);
    }
}