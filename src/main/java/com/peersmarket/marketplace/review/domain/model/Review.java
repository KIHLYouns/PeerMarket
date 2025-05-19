package com.peersmarket.marketplace.review.domain.model;

import java.time.LocalDateTime;

import com.peersmarket.marketplace.user.domain.model.AppUser; // Assurez-vous que le chemin est correct

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    private Long id;
    private int rating; // e.g., 1 to 5
    private String comment;
    private AppUser reviewer;
    private AppUser reviewee;
    private LocalDateTime createdAt;
}
