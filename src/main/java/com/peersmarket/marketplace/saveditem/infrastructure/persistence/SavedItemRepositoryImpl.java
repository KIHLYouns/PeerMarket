package com.peersmarket.marketplace.saveditem.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.saveditem.application.port.out.SavedItemRepository;
import com.peersmarket.marketplace.saveditem.domain.model.SavedItem;
import com.peersmarket.marketplace.saveditem.infrastructure.persistence.jpa.mapper.SavedItemMapper;
import com.peersmarket.marketplace.saveditem.infrastructure.persistence.jpa.model.SavedItemEntity;
import com.peersmarket.marketplace.saveditem.infrastructure.persistence.jpa.repository.SavedItemJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SavedItemRepositoryImpl implements SavedItemRepository {

    private final SavedItemJpaRepository savedItemJpaRepository;
    private final SavedItemMapper savedItemMapper;
    // Les champs suivants ne sont pas directement utilisés par SavedItemRepositoryImpl
    // mais sont listés ici pour montrer la dépendance si on devait charger User/Item Entities.
    // private final AppUserRepository appUserRepository;
    // private final ItemRepository itemRepository;

    @Override
    @Transactional
    public SavedItem save(final SavedItem savedItem) {
        // La logique de chargement de AppUserEntity et ItemEntity doit être gérée avant d'appeler toEntity
        // ou dans le mapper si les IDs sont suffisants pour créer des références.
        // Pour cet exemple, on suppose que savedItem.getUser() et savedItem.getItem() sont des objets de domaine complets
        // ou que le mapper peut gérer la conversion à partir d'IDs si nécessaire.
        final SavedItemEntity entity = savedItemMapper.toEntity(savedItem);
        // Assurez-vous que les entités User et Item sont correctement définies dans 'entity'
        // Si 'savedItem' contient des objets User/Item complets, le mapper devrait les gérer.
        // Si 'savedItem' ne contient que des IDs, le mapper ou ce service devrait charger les entités.
        // Par exemple, si le mapper ne le fait pas :
        // if (savedItem.getUser() != null && savedItem.getUser().getId() != null) {
        //    AppUserEntity userEntity = appUserRepository.findById(savedItem.getUser().getId())
        //        .map(appUserMapper::toEntity) // en supposant un appUserMapper
        //        .orElseThrow(() -> new RuntimeException("User not found"));
        //    entity.setUser(userEntity);
        // }
        // if (savedItem.getItem() != null && savedItem.getItem().getId() != null) {
        //    ItemEntity itemEntity = itemRepository.findById(savedItem.getItem().getId())
        //        .map(itemMapper::toEntity) // en supposant un itemMapper
        //        .orElseThrow(() -> new RuntimeException("Item not found"));
        //    entity.setItem(itemEntity);
        // }
        final SavedItemEntity savedEntity = savedItemJpaRepository.save(entity);
        return savedItemMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SavedItem> findById(final Long id) {
        return savedItemJpaRepository.findById(id).map(savedItemMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavedItem> findByUserId(final Long userId) {
        return savedItemJpaRepository.findByUserId(userId).stream()
                .map(savedItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SavedItem> findByUserIdAndItemId(final Long userId, final Long itemId) {
        return savedItemJpaRepository.findByUserIdAndItemId(userId, itemId).map(savedItemMapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteByUserIdAndItemId(final Long userId, final Long itemId) {
        savedItemJpaRepository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndItemId(final Long userId, final Long itemId) {
        return savedItemJpaRepository.existsByUserIdAndItemId(userId, itemId);
    }

    @Override
    @Transactional
    public void deleteById(final Long id) {
        savedItemJpaRepository.deleteById(id);
    }
}
