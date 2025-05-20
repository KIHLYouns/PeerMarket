package com.peersmarket.marketplace.message.application.port.out;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.peersmarket.marketplace.message.domain.model.Message;

public interface MessageRepository {
    Message save(Message message);
    Optional<Message> findById(Long id);
    Page<Message> findByConversationIdOrderByTimestampDesc(Long conversationId, Pageable pageable);
    List<Message> findAllByConversationId(Long conversationId); // Ajouter cette méthode
    // Optionnel mais mieux pour la performance lors de la sauvegarde de plusieurs entités
    List<Message> saveAll(List<Message> messages); 
}
