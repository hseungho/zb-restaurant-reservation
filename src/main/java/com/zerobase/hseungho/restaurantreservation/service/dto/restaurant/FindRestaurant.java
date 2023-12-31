package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class FindRestaurant {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long restaurantId;
        private String name;
        private String address;
        private String description;
        private Double rating;
        private List<MenuResponse> menus;
        private String openTime;
        private String closeTime;
        private Integer maxPerReservation;
        private String contactNumber;
        private List<ReviewResponse> reviews;
        public static Response fromDto(RestaurantDto dto) {
            return Response.builder()
                    .restaurantId(dto.getId())
                    .name(dto.getName())
                    .address(dto.getAddress())
                    .description(dto.getDescription())
                    .rating(dto.getRating())
                    .menus(dto.getMenus().stream().map(MenuResponse::fromDto).toList())
                    .openTime(dto.getOpenTime().toString())
                    .closeTime(dto.getCloseTime().toString())
                    .maxPerReservation(dto.getMaxPerReservation())
                    .contactNumber(dto.getContactNumber())
                    .reviews(dto.getReviews().stream().map(ReviewResponse::fromDto).toList())
                    .build();
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        private static class MenuResponse {
            private Long id;
            private String name;
            private Long price;
            public static MenuResponse fromDto(MenuDto dto) {
                return MenuResponse.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .price(dto.getPrice())
                        .build();
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        private static class ReviewResponse {
            private Long reviewId;
            private String authorNickname;
            private Double rating;
            private String content;
            private String imageSrc;
            private String createdAt;
            private String updatedAt;
            private static ReviewResponse fromDto(ReviewDto dto) {
                return ReviewResponse.builder()
                        .reviewId(dto.getId())
                        .authorNickname(dto.getAuthorNickname())
                        .rating(dto.getRating())
                        .content(dto.getContent())
                        .imageSrc(dto.getImageSrc())
                        .createdAt(dto.getCreatedAt().toString())
                        .updatedAt(dto.getUpdatedAt().toString())
                        .build();
            }
        }
    }
}
