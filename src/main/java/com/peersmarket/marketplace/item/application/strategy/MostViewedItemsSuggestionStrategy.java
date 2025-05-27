package com.peersmarket.marketplace.item.application.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.peersmarket.marketplace.item.application.port.out.ItemRepository;
import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.item.domain.model.ItemStatus;

import lombok.RequiredArgsConstructor;

@Component("mostViewedItemsStrategy")
@RequiredArgsConstructor
public class MostViewedItemsSuggestionStrategy implements ItemSuggestionStrategy {

    private final ItemRepository itemRepository;

    @Override
    public List<Item> getSuggestedItems() {
        return itemRepository.findByStatusOrderByViewCountDescCreatedAtDesc(ItemStatus.AVAILABLE);
    }
}
