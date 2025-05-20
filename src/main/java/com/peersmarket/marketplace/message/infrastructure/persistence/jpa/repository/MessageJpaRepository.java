package com.peersmarket.marketplace.message.infrastructure.persistence.jpa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peersmarket.marketplace.message.infrastructure.persistence.jpa.model.MessageEntity;

public interface MessageJpaRepository extends JpaRepository<MessageEntity, Long> {

    @Query("SELECT m FROM MessageEntity m WHERE m.conversation.id = :conversationId ORDER BY m.timestamp DESC")
    Page<MessageEntity> findByConversationIdOrderByTimestampDesc(@Param("conversationId") Long conversationId, Pageable pageable);

    @Query("SELECT m FROM MessageEntity m WHERE m.conversation.id = :conversationId ORDER BY m.timestamp ASC")
    List<MessageEntity> findByConversationId(@Param("conversationId") Long conversationId);
}
