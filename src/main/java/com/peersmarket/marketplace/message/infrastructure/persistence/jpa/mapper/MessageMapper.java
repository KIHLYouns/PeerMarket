package com.peersmarket.marketplace.message.infrastructure.persistence.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.peersmarket.marketplace.conversation.infrastructure.persistence.jpa.mapper.ConversationMapper; // À créer
import com.peersmarket.marketplace.message.application.dto.MessageDto;
import com.peersmarket.marketplace.message.domain.model.Message;
import com.peersmarket.marketplace.message.infrastructure.persistence.jpa.model.MessageEntity;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.AppUserMapper; // Assurez-vous que ce mapper existe

@Mapper(componentModel = "spring", uses = {AppUserMapper.class, ConversationMapper.class})
public interface MessageMapper {

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "sender.username", target = "senderUsername")
    @Mapping(source = "conversation.id", target = "conversationId")
    MessageDto toDto(Message message);

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "sender.username", target = "senderUsername")
    @Mapping(source = "conversation.id", target = "conversationId")
    MessageDto toDto(MessageEntity entity);
    
    @Mapping(source = "senderId", target = "sender.id")
    @Mapping(source = "conversationId", target = "conversation.id")
    @Mapping(source = "read", target = "isRead")
    Message toDomain(MessageDto dto); // Moins courant, mais possible

    @Mapping(target = "sender", source = "sender") // AppUserMapper s'en chargera
    @Mapping(target = "conversation", source = "conversation") // ConversationMapper s'en chargera
    @Mapping(source = "read", target = "isRead")
    Message toDomain(MessageEntity entity);

    @Mapping(target = "sender", ignore = true) // Gérer manuellement via des appels aux repositories si besoin de charger l'entité complète
    @Mapping(target = "conversation", ignore = true) // Idem
    MessageEntity toEntity(Message domain);
}
