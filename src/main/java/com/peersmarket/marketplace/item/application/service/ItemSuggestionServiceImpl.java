package com.peersmarket.marketplace.item.application.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.item.application.dto.ItemDto;
import com.peersmarket.marketplace.item.application.port.in.ItemSuggestionService;
import com.peersmarket.marketplace.item.application.strategy.ItemSuggestionStrategy;
import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.item.infrastructure.persistence.jpa.mapper.ItemMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemSuggestionServiceImpl implements ItemSuggestionService {

    private final Map<String, ItemSuggestionStrategy> strategies;
    private final String defaultStrategyName;
    private final ItemMapper itemMapper;

    public ItemSuggestionServiceImpl(Map<String, ItemSuggestionStrategy> strategies,
                                   @Value("${app.item-suggestions.default-strategy:recentItemsStrategy}") String defaultStrategyName,
                                   ItemMapper itemMapper) {
        this.strategies = strategies;
        this.defaultStrategyName = defaultStrategyName;
        this.itemMapper = itemMapper;
        log.info("ItemSuggestionService initialized with default strategy: {} and available strategies: {}",
                 defaultStrategyName, strategies.keySet());
    }

    @Override
    public List<ItemDto> getSuggestedItems() {
        return getSuggestedItems(defaultStrategyName);
    }

    @Override
    public List<ItemDto> getSuggestedItems(String strategyName) {
        ItemSuggestionStrategy strategy = strategies.get(strategyName);
        if (strategy == null) {
            log.warn("ItemSuggestionStrategy with name '{}' not found. Falling back to default strategy '{}'.", strategyName, defaultStrategyName);
            strategy = strategies.get(defaultStrategyName);
            if (strategy == null) {
                log.error("Default ItemSuggestionStrategy '{}' not found. Cannot retrieve suggested items.", defaultStrategyName);
                throw new IllegalStateException("Default item suggestion strategy not configured: " + defaultStrategyName);
            }
        }
        log.info("Using strategy '{}' to fetch suggested items.", strategy.getClass().getSimpleName());
        List<Item> items = strategy.getSuggestedItems();
        return items.stream().map(itemMapper::toDto).collect(Collectors.toList());
    }
}
