package com.peersmarket.marketplace.item.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.peersmarket.marketplace.item.domain.model.ItemCondition;
import com.peersmarket.marketplace.item.domain.model.ItemStatus;
import com.peersmarket.marketplace.user.application.dto.AppUserDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private ItemCondition condition;
    private ItemStatus status;
    private Long sellerId;
    private Long categoryId;

    private AppUserDto sellerInfo;
    private CategoryDto categoryInfo;

    private LocalDateTime createdAt;
    private List<ImageDto> images = new ArrayList<>();
}
