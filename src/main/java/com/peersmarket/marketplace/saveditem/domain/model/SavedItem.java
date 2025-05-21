package com.peersmarket.marketplace.saveditem.domain.model;

import java.time.LocalDateTime;

import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.user.domain.model.AppUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedItem {
    private Long id;
    private AppUser user;
    private Item item;
    private LocalDateTime savedAt;
}