package com.peersmarket.marketplace.conversation.infrastructure.persistence.jpa.repository;

import com.peersmarket.marketplace.conversation.infrastructure.persistence.jpa.model.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationJpaRepository extends JpaRepository<ConversationEntity, Long> {

    @Query("SELECT c FROM ConversationEntity c JOIN c.participants p WHERE p.id = :userId ORDER BY c.updatedAt DESC")
    List<ConversationEntity> findByParticipantId(@Param("userId") Long userId);

    // Pour trouver une conversation existante pour un item entre deux utilisateurs.
    // Cette requête suppose que vous voulez une conversation où les DEUX utilisateurs sont participants.
    @Query("SELECT c FROM ConversationEntity c " +
           "JOIN c.participants p1 " +
           "JOIN c.participants p2 " +
           "WHERE c.item.id = :itemId " +
           "AND p1.id = :userId1 " +
           "AND p2.id = :userId2")
    Optional<ConversationEntity> findByItemIdAndExactParticipants(@Param("itemId") Long itemId, @Param("userId1") Long userId1, @Param("userId2") Long userId2);
}