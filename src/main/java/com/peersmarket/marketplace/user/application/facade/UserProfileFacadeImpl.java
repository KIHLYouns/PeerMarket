package com.peersmarket.marketplace.user.application.facade;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peersmarket.marketplace.item.application.dto.ItemDto;
import com.peersmarket.marketplace.item.application.port.in.ItemService;
import com.peersmarket.marketplace.review.application.dto.ReviewDto;
import com.peersmarket.marketplace.review.application.port.in.ReviewService;
import com.peersmarket.marketplace.saveditem.application.dto.SavedItemDto;
import com.peersmarket.marketplace.saveditem.application.port.in.SavedItemService;
import com.peersmarket.marketplace.user.application.dto.AppUserDto;
import com.peersmarket.marketplace.user.application.dto.UserProfileViewDto;
import com.peersmarket.marketplace.user.application.port.in.AppUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileFacadeImpl implements UserProfileFacade {

    private final AppUserService appUserService;
    private final ItemService itemService;
    private final ReviewService reviewService;
    private final SavedItemService savedItemService;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileViewDto> getUserProfile(final Long userId) {
        final Optional<AppUserDto> userOpt = appUserService.getUserById(userId);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        final AppUserDto userDto = userOpt.get();

        final List<ItemDto> itemsListed = itemService.getItemsBySellerId(userId);
        final List<ReviewDto> reviewsReceived = reviewService.getReviewsForUser(userId);
        final List<ReviewDto> reviewsGiven = reviewService.getReviewsByReviewer(userId);
        final List<SavedItemDto> savedItems = savedItemService.getSavedItemsByUserId(userId);

        final UserProfileViewDto profileViewDto = new UserProfileViewDto(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getAvatarUrl(),
                userDto.getBio(),
                userDto.getJoinDate(),
                userDto.getAddress(),
                userDto.getVerified(),
                itemsListed,
                reviewsReceived,
                reviewsGiven,
                savedItems);
        return Optional.of(profileViewDto);
    }
}
