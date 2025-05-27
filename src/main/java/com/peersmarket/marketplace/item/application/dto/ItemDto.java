package com.peersmarket.marketplace.item.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.peersmarket.marketplace.item.domain.model.ItemCondition;
import com.peersmarket.marketplace.item.domain.model.ItemStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private ItemCondition condition;
    private ItemStatus status;
    private Long sellerId;
    private String sellerUsername;
    private String sellerAvatarUrl;
    private Double sellerAverageRating;
    private Integer sellerRatingCount;
    private Long sellerCityId;
    private String sellerCityName;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private Integer viewCount;
    private List<ImageDto> images;
}
