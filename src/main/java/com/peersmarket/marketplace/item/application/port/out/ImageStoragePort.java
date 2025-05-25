package com.peersmarket.marketplace.item.application.port.out;

import com.peersmarket.marketplace.item.application.model.ImageFileWrapper;

public interface ImageStoragePort {
    /**
     * Stocke une image et retourne son URL publique ou un identifiant unique.
     * @param imageFileWrapper L'objet contenant les données de l'image.
     * @return L'URL publique ou l'identifiant de l'image stockée.
     * @throws RuntimeException si une erreur survient lors du stockage.
     */
    String storeImage(ImageFileWrapper imageFileWrapper);

    /**
     * Supprime une image du stockage.
     * @param imageIdentifier L'identifiant de l'image (URL ou clé).
     * @throws RuntimeException si une erreur survient lors de la suppression.
     */
    void deleteImage(String imageIdentifier);

    // Optionnel: si vous avez besoin de récupérer l'URL publique à partir d'une clé
    // String getPublicUrl(String imageKey);
}
