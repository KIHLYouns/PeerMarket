package com.peersmarket.marketplace.item.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.item.application.port.out.ImageRepository;
import com.peersmarket.marketplace.item.domain.model.Image;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.ImageMapper;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.model.ImageEntity;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.model.ItemEntity;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.repository.ImageJpaRepository;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.repository.ItemJpaRepository;
import com.peersmarket.marketplace.shared.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {

    private final ImageJpaRepository imageJpaRepository;
    private final ItemJpaRepository itemJpaRepository; // Ajouté pour lier l'image à l'item
    private final ImageMapper imageMapper;

    @Override
    @Transactional
    public Image save(final Image image) {
        final ImageEntity imageEntity = imageMapper.toEntity(image); // image.getUrl() contient l'URL S3

        // L'association à ItemEntity est cruciale pour que JPA sache à quel item cette image appartient.
        if (image.getItemId() != null) {
            final ItemEntity itemEntity = itemJpaRepository.findById(image.getItemId())
                .orElseThrow(() -> new NotFoundException("Item non trouvé pour l'image avec itemId : " + image.getItemId()));
            imageEntity.setItem(itemEntity); // Assigner l'entité Item gérée
        } else {
            // Ce cas ne devrait pas arriver si la logique du service est correcte.
            // L'URL est externe, mais l'association à un item dans notre DB est toujours nécessaire.
            throw new IllegalStateException("L'image doit être associée à un article (itemId doit être défini) pour être sauvegardée en base de données.");
        }
        final ImageEntity savedEntity = imageJpaRepository.save(imageEntity);
        return imageMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Image> findById(final Long id) {
        return imageJpaRepository.findById(id).map(imageMapper::toDomain);
    }

    @Override
    public Optional<Image> findByIdAndItemId(final Long id, final Long itemId) {
        return imageJpaRepository.findByIdAndItemId(id, itemId).map(imageMapper::toDomain);
    }

    @Override
    public List<Image> findByItemId(final Long itemId) {
        return imageMapper.toDomainListFromEntity(imageJpaRepository.findByItemId(itemId));
    }

    @Override
    @Transactional
    public void deleteById(final Long id) {
        imageJpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByIdAndItemId(final Long id, final Long itemId) {
        // S'assurer que l'image existe et appartient à l'item avant de supprimer
        final ImageEntity imageEntity = imageJpaRepository.findByIdAndItemId(id, itemId)
            .orElseThrow(() -> new NotFoundException("Image avec ID " + id + " non trouvée pour l'item ID " + itemId));
        imageJpaRepository.delete(imageEntity);
    }
}
