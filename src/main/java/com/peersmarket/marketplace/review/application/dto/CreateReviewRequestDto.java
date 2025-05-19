package com.peersmarket.marketplace.review.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequestDto {

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Comment cannot be blank")
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;

    @NotNull(message = "Reviewee ID cannot be null")
    private Long revieweeId;

    @NotNull(message = "Reviewer ID cannot be null")
    private Long reviewerId;
}
