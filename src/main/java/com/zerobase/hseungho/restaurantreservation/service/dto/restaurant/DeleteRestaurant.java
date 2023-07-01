package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DeleteRestaurant {
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private RestaurantResponse restaurant;
        public static Response fromDto(RestaurantDto dto) {
            return Response.builder()
                    .restaurant(RestaurantResponse.fromDto(dto))
                    .build();
        }

        @Data @NoArgsConstructor @AllArgsConstructor @Builder
        private static class RestaurantResponse {
            private Long id;
            private String deleteReqAt;
            private String deletedAt;
            private static RestaurantResponse fromDto(RestaurantDto dto) {
                return RestaurantResponse.builder()
                        .id(dto.getId())
                        .deleteReqAt(dto.getDeleteReqAt().toString())
                        .deletedAt(dto.getDeletedAt().toString())
                        .build();
            }
        }
    }
}
