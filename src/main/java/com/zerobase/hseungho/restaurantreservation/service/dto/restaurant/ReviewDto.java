package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long id;
    private Double rating;
    private String content;
    private String imageSrc;
    private String authorId;
    private String authorNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewDto fromEntity(Review entity) {
        return ReviewDto.builder()
                .id(entity.getId())
                .rating(entity.getRating())
                .content(entity.getContent())
                .imageSrc(entity.getImageSrc())
                .authorId(entity.getAuthor().getId())
                .authorNickname(entity.getAuthor().getNickname())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

}
