package com.peersmarket.marketplace.conversation.infrastructure.web;

import com.peersmarket.marketplace.conversation.application.dto.ConversationDto;
import com.peersmarket.marketplace.conversation.application.dto.CreateConversationRequestDto;
import com.peersmarket.marketplace.conversation.application.port.in.ConversationService;
import com.peersmarket.marketplace.message.application.dto.CreateMessageRequestDto;
import com.peersmarket.marketplace.message.application.dto.MessageDto;
import com.peersmarket.marketplace.message.application.port.in.MessageService;
import com.peersmarket.marketplace.shared.exception.ForbiddenException;
import com.peersmarket.marketplace.shared.exception.NotFoundException;
// import com.peersmarket.marketplace.user.domain.model.AppUser; // Si vous utilisez un UserDetails personnalisé
// import org.springframework.security.core.annotation.AuthenticationPrincipal; // Pour l'utilisateur authentifié
// import org.springframework.security.core.userdetails.UserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ConversationDto> createConversation(
            @Valid @RequestBody CreateConversationRequestDto createDto
            // @AuthenticationPrincipal UserDetails currentUser // Décommenter avec Spring Security
    ) {
        // Long authenticatedUserId = getAuthenticatedUserId(currentUser);
        // if (!authenticatedUserId.equals(createDto.getInitiatorId())) {
        //     throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Initiator ID must match authenticated user.");
        // }
        try {
            ConversationDto conversationDto = conversationService.createConversation(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(conversationDto);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<ConversationDto> getConversationById(
            @PathVariable Long conversationId
            // @AuthenticationPrincipal UserDetails currentUser
    ) {
        // TODO: Vérifier si currentUser a accès à cette conversation
        return conversationService.getConversationById(conversationId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found with id: " + conversationId));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ConversationDto>> getConversationsForUser(
            @PathVariable Long userId
            // @AuthenticationPrincipal UserDetails currentUser
    ) {
        // Long authenticatedUserId = getAuthenticatedUserId(currentUser);
        // if (!authenticatedUserId.equals(userId)) {
        //     throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot access conversations of another user.");
        // }
        try {
            return ResponseEntity.ok(conversationService.getConversationsForUser(userId));
        } catch (NotFoundException e) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/{conversationId}/messages")
    public ResponseEntity<MessageDto> sendMessageToConversation(
            @PathVariable Long conversationId,
            @Valid @RequestBody CreateMessageRequestDto createMessageDto
            // @AuthenticationPrincipal UserDetails currentUser
    ) {
        // Long authenticatedUserId = getAuthenticatedUserId(currentUser);
        // if (!authenticatedUserId.equals(createMessageDto.getSenderId())) {
        //     throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sender ID must match authenticated user.");
        // }
        if (!conversationId.equals(createMessageDto.getConversationId())) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conversation ID in path ("+conversationId+") and body ("+createMessageDto.getConversationId()+") must match.");
        }
        try {
            MessageDto messageDto = messageService.sendMessage(createMessageDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<MessageDto>> getMessagesForConversation(
            @PathVariable Long conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
            // @AuthenticationPrincipal UserDetails currentUser
    ) {
        // TODO: Vérifier si currentUser a accès à cette conversation
        try {
            return ResponseEntity.ok(conversationService.getMessagesForConversation(conversationId, page, size));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{conversationId}/messages/mark-as-read")
    public ResponseEntity<List<MessageDto>> markAllMessagesAsRead(
            @PathVariable final Long conversationId,
            @RequestParam final Long readerId // Added readerId as a request parameter
            // @AuthenticationPrincipal UserDetails currentUser // Décommenter avec Spring Security
    ) {
        // Long authenticatedUserId = getAuthenticatedUserId(currentUser);

        // if (authenticatedUserId == null) {
        //     throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        // }
        // else if (!authenticatedUserId.equals(readerId)) {
        //     throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Reader ID must match authenticated user.");
        // }


        try {
            final List<MessageDto> updatedMessages = messageService.markAllMessagesAsRead(conversationId, readerId);
            return ResponseEntity.ok(updatedMessages);
        } catch (final NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (final ForbiddenException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (final IllegalArgumentException e) { 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /*
    private Long getAuthenticatedUserId(UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        // Adaptez ceci à votre implémentation de UserDetails
        // Par exemple, si votre UserDetails est une instance de AppUser (domaine) ou une classe UserDetails personnalisée
        if (userDetails instanceof com.peersmarket.marketplace.user.domain.model.AppUser) { // Exemple
             return ((com.peersmarket.marketplace.user.domain.model.AppUser) userDetails).getId();
        }
        // Ou si vous stockez l'ID dans le username ou une autre propriété
        // return Long.parseLong(userDetails.getUsername()); // Si username est l'ID
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot determine authenticated user ID");
    }
    */
}