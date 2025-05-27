package com.peersmarket.marketplace.item.application.strategy;

import java.util.List;

import com.peersmarket.marketplace.item.domain.model.Item;

/**
 * Interface pour les stratégies de sélection des suggestions d'articles.
 */
public interface ItemSuggestionStrategy {
    /**
     * Récupère une liste d'articles suggérés selon la logique de la stratégie.
     *
     * @return Une liste d'objets Item.
     */
    List<Item> getSuggestedItems();
}
