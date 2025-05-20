package com.peersmarket.marketplace.conversation.infrastructure.persistence.jpa.mapper;

import com.peersmarket.marketplace.conversation.application.dto.ConversationDto;
import com.peersmarket.marketplace.conversation.domain.model.Conversation;
import com.peersmarket.marketplace.conversation.infrastructure.persistence.jpa.model.ConversationEntity;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.ItemMapper;
import com.peersmarket.marketplace.user.domain.model.AppUser; // Import pour AppUser (domaine)
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.AppUserMapper;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model.AppUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AppUserMapper.class, ItemMapper.class})
public interface ConversationMapper {

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.title", target = "itemTitle")
    @Mapping(source = "participants", target = "participantIds", qualifiedByName = "appUserToId") 
    @Mapping(source = "participants", target = "participantUsernames", qualifiedByName = "appUserToUsername") 
    @Mapping(source = "lastMessageSender.id", target = "lastMessageSenderId")
    @Mapping(source = "lastMessageSender.username", target = "lastMessageSenderUsername")
    ConversationDto toDto(Conversation conversation); // C'est cette méthode qui avait les erreurs

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.title", target = "itemTitle")
    @Mapping(source = "participants", target = "participantIds", qualifiedByName = "appUserEntityToId")
    @Mapping(source = "participants", target = "participantUsernames", qualifiedByName = "appUserEntityToUsername")
    @Mapping(source = "lastMessageSender.id", target = "lastMessageSenderId")
    @Mapping(source = "lastMessageSender.username", target = "lastMessageSenderUsername")
    ConversationDto toDto(ConversationEntity entity);


    @Mapping(target = "item", source = "item")
    @Mapping(target = "participants", source = "participants")
    @Mapping(target = "lastMessageSender", source = "lastMessageSender")
    Conversation toDomain(ConversationEntity entity);

    @Mapping(target = "item", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "lastMessageSender", ignore = true)
    ConversationEntity toEntity(Conversation domain); // Renommé de toEntity à domainToEntity pour clarté

    // Qualificateurs pour List<AppUserEntity> (JPA)
    @Named("appUserEntityToId")
    default List<Long> appUserEntityToId(List<AppUserEntity> participants) {
        if (participants == null) return null;
        return participants.stream().map(AppUserEntity::getId).collect(Collectors.toList());
    }

    @Named("appUserEntityToUsername")
    default List<String> appUserEntityToUsername(List<AppUserEntity> participants) {
        if (participants == null) return null;
        return participants.stream().map(AppUserEntity::getUsername).collect(Collectors.toList());
    }

    // Nouveaux qualificateurs pour List<AppUser> (Domaine)
    @Named("appUserToId")
    default List<Long> appUserToId(List<AppUser> participants) {
        if (participants == null) return null;
        return participants.stream().map(AppUser::getId).collect(Collectors.toList());
    }

    @Named("appUserToUsername")
    default List<String> appUserToUsername(List<AppUser> participants) {
        if (participants == null) return null;
        return participants.stream().map(AppUser::getUsername).collect(Collectors.toList());
    }
}