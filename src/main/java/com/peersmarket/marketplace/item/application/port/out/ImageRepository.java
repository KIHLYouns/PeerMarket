package com.peersmarket.marketplace.item.application.port.out;

import java.util.List;
import java.util.Optional;

import com.peersmarket.marketplace.item.domain.model.Image;

public interface ImageRepository {
    Image save(Image image);
    Optional<Image> findById(Long id);
    Optional<Image> findByIdAndItemId(Long id, Long itemId);
    List<Image> findByItemId(Long itemId);
    void deleteById(Long id);
    void deleteByIdAndItemId(Long id, Long itemId);
}
