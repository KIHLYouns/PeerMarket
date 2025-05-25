package com.peersmarket.marketplace.user.application.dto;

import com.peersmarket.marketplace.item.application.dto.ItemDto;
import com.peersmarket.marketplace.review.application.dto.ReviewDto;
import com.peersmarket.marketplace.saveditem.application.dto.SavedItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileViewDto {
    private Long id;
    private String username;
    private String email;
    private String avatarUrl;
    private String bio;
    private LocalDate joinDate;
    private AddressDto address;
    private boolean verified;

    private List<ItemDto> itemsListed;
    private List<ReviewDto> reviewsReceived;
    private List<ReviewDto> reviewsGiven;
    private List<SavedItemDto> savedItems;
}