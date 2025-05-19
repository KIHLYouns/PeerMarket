package com.peersmarket.marketplace.review.application.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private int rating;
    private String comment;
    private Long reviewerId;
    private String reviewerUsername; // Pour afficher facilement qui a écrit l'avis
    private Long revieweeId;
    private String revieweeUsername; // Pour afficher facilement qui a été évalué
    private LocalDateTime createdAt;
}