package com.zerobase.hseungho.restaurantreservation.service.dto.review;

import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReservationDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UpdateReview {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Double rating;
        private String content;
        private Boolean isDeleteImage;
        private Boolean isUpdateImage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private RestaurantResponse restaurant;
        private ReservationResponse reservation;
        private ReviewResponse review;

        public static Response fromDto(ReviewDto dto) {
            return Response.builder()
                    .restaurant(RestaurantResponse.fromDto(dto.getRestaurant()))
                    .reservation(ReservationResponse.fromDto(dto.getReservation()))
                    .review(ReviewResponse.fromDto(dto))
                    .build();
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        private static class RestaurantResponse {
            private Long id;
            private String name;
            private String address;
            private static RestaurantResponse fromDto(RestaurantDto dto) {
                return RestaurantResponse.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .address(dto.getAddress())
                        .build();
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        private static class ReservationResponse {
            private Long id;
            private String number;
            private String visitedAt;
            private static ReservationResponse fromDto(ReservationDto dto) {
                return ReservationResponse.builder()
                        .id(dto.getId())
                        .number(dto.getNumber())
                        .visitedAt(dto.getVisitedAt().toString())
                        .build();
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        private static class ReviewResponse {
            private Long id;
            private Double rating;
            private String content;
            private String imageSrc;
            private String authorNickname;
            private static ReviewResponse fromDto(ReviewDto dto) {
                return ReviewResponse.builder()
                        .id(dto.getId())
                        .rating(dto.getRating())
                        .content(dto.getContent())
                        .imageSrc(dto.getImageSrc())
                        .authorNickname(dto.getAuthor().getNickname())
                        .build();
            }
        }
    }
}
