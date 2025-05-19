package com.peersmarket.marketplace.review.infrastructure.persistence.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.peersmarket.marketplace.review.application.dto.ReviewDto;
import com.peersmarket.marketplace.review.domain.model.Review;
import com.peersmarket.marketplace.review.infrastructure.persistence.jpa.model.ReviewEntity;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.AppUserMapper;

@Mapper(componentModel = "spring", uses = {AppUserMapper.class} )
public interface ReviewMapper {

    Review toDomain(ReviewEntity entity);

    ReviewEntity toEntity(Review domain);

    @Mapping(source = "reviewer.id", target = "reviewerId")
    @Mapping(source = "reviewer.username", target = "reviewerUsername")
    @Mapping(source = "reviewee.id", target = "revieweeId")
    @Mapping(source = "reviewee.username", target = "revieweeUsername")
    ReviewDto toDto(Review domain);
    
}
