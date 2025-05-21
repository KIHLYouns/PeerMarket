package com.peersmarket.marketplace.saveditem.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.item.application.port.out.ItemRepository;
import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.saveditem.application.dto.CreateSavedItemDto;
import com.peersmarket.marketplace.saveditem.application.dto.SavedItemDto;
import com.peersmarket.marketplace.saveditem.application.port.in.SavedItemService;
import com.peersmarket.marketplace.saveditem.application.port.out.SavedItemRepository;
import com.peersmarket.marketplace.saveditem.domain.model.SavedItem;
import com.peersmarket.marketplace.saveditem.infrastructure.persistence.jpa.mapper.SavedItemMapper;
import com.peersmarket.marketplace.shared.exception.AlreadyExistsException;
import com.peersmarket.marketplace.shared.exception.NotFoundException;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;
import com.peersmarket.marketplace.user.domain.model.AppUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SavedItemServiceImpl implements SavedItemService {

    private final SavedItemRepository savedItemRepository;
    private final AppUserRepository appUserRepository;
    private final ItemRepository itemRepository;
    private final SavedItemMapper savedItemMapper;

    @Override
    public SavedItemDto saveItem(final CreateSavedItemDto createSavedItemDto) {
        final Long userId = createSavedItemDto.getUserId();
        final Long itemId = createSavedItemDto.getItemId();

        if (savedItemRepository.existsByUserIdAndItemId(userId, itemId)) {
            throw new AlreadyExistsException("L'article ID " + itemId + " est déjà sauvegardé par l'utilisateur ID " + userId);
        }

        final AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        final Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Article non trouvé avec l'ID : " + itemId));

        final SavedItem savedItem = SavedItem.builder()
                .user(user)
                .item(item)
                .savedAt(LocalDateTime.now())
                .build();

        final SavedItem result = savedItemRepository.save(savedItem);
        return savedItemMapper.toDto(result);
    }

    @Override
    public void unsaveItem(final Long userId, final Long itemId) {
        if (!savedItemRepository.existsByUserIdAndItemId(userId, itemId)) {
            throw new NotFoundException("Aucun article sauvegardé trouvé pour l'utilisateur ID " + userId + " et l'article ID " + itemId);
        }
        // Valider l'existence de l'utilisateur et de l'item n'est pas strictement nécessaire ici
        // car on supprime basé sur l'existence de la sauvegarde elle-même.
        savedItemRepository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavedItemDto> getSavedItemsByUserId(final Long userId) {
        if (!appUserRepository.existsById(userId)) { // Vérification de l'existence de l'utilisateur
            throw new NotFoundException("Utilisateur non trouvé avec l'ID : " + userId);
        }
        final List<SavedItem> savedItems = savedItemRepository.findByUserId(userId);
        return savedItemMapper.toDtoList(savedItems);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isItemSavedByUser(final Long userId, final Long itemId) {
        // Pas besoin de vérifier l'existence de l'utilisateur ou de l'item ici,
        // la requête de vérification suffit.
        return savedItemRepository.existsByUserIdAndItemId(userId, itemId);
    }
}
