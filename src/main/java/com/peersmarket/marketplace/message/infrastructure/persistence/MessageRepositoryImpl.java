package com.peersmarket.marketplace.message.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.conversation.application.port.out.ConversationRepository;
import com.peersmarket.marketplace.conversation.infrastructure.persistence.jpa.mapper.ConversationMapper;
import com.peersmarket.marketplace.message.application.port.out.MessageRepository;
import com.peersmarket.marketplace.message.domain.model.Message;
import com.peersmarket.marketplace.message.infrastructure.persistence.jpa.mapper.MessageMapper;
import com.peersmarket.marketplace.message.infrastructure.persistence.jpa.model.MessageEntity;
import com.peersmarket.marketplace.message.infrastructure.persistence.jpa.repository.MessageJpaRepository;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.AppUserMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {

    private final MessageJpaRepository messageJpaRepository;
    private final MessageMapper messageMapper;
    private final AppUserRepository appUserRepository; // Pour mapper sender
    private final AppUserMapper appUserMapper;
    private final ConversationRepository conversationRepository; // Pour mapper conversation
    private final ConversationMapper conversationMapper;

    @Override
    @Transactional
    public Message save(final Message message) {
        final MessageEntity messageEntity = messageMapper.toEntity(message);
        // Assurer que les entités associées sont correctement mappées ou récupérées
        if (message.getSender() != null && message.getSender().getId() != null) {
            appUserRepository.findById(message.getSender().getId())
                .map(appUserMapper::toEntity) // Assurez-vous que AppUserMapper a toEntity
                .ifPresent(messageEntity::setSender);
        }
        if (message.getConversation() != null && message.getConversation().getId() != null) {
            conversationRepository.findById(message.getConversation().getId())
                .map(conversationMapper::toEntity) // Assurez-vous que ConversationMapper a domainToEntity
                .ifPresent(messageEntity::setConversation);
        }
        final MessageEntity savedEntity = messageJpaRepository.save(messageEntity);
        return messageMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Message> findById(final Long id) {
        return messageJpaRepository.findById(id).map(messageMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Message> findByConversationIdOrderByTimestampDesc(final Long conversationId, final Pageable pageable) {
        return messageJpaRepository.findByConversationIdOrderByTimestampDesc(conversationId, pageable)
                .map(messageMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findAllByConversationId(final Long conversationId) {
        return messageJpaRepository.findByConversationId(conversationId).stream()
                .map(messageMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Message> saveAll(final List<Message> messages) {
        List<MessageEntity> messageEntities = messages.stream()
                .map(domainMessage -> {
                    MessageEntity entity = messageMapper.toEntity(domainMessage);
                    // Réassigner manuellement les entités sender et conversation
                    if (domainMessage.getSender() != null && domainMessage.getSender().getId() != null) {
                        // Important : Il faut s'assurer que l'entité récupérée est managée par la session Hibernate.
                        // findById retourne une entité managée.
                        appUserRepository.findById(domainMessage.getSender().getId())
                            .map(appUserMapper::toEntity) // Assurez-vous que AppUserMapper.toEntity retourne AppUserEntity
                            .ifPresent(entity::setSender);
                    } else if (domainMessage.getSender() != null) {
                        // Si l'objet sender existe mais n'a pas d'ID, c'est un problème de données.
                        // Vous pourriez vouloir logger une erreur ou lever une exception.
                        // Pour l'instant, on laisse le sender de l'entité à null, ce qui causera une erreur si non nullable.
                    }

                    if (domainMessage.getConversation() != null && domainMessage.getConversation().getId() != null) {
                        conversationRepository.findById(domainMessage.getConversation().getId())
                            .map(conversationMapper::toEntity) // Assurez-vous que ConversationMapper.toEntity retourne ConversationEntity
                            .ifPresent(entity::setConversation);
                    } else if (domainMessage.getConversation() != null) {
                        // Idem pour la conversation
                    }
                    return entity;
                })
                .collect(Collectors.toList());

        List<MessageEntity> savedEntities = messageJpaRepository.saveAll(messageEntities);
        return savedEntities.stream()
                .map(messageMapper::toDomain)
                .collect(Collectors.toList());
    }
}