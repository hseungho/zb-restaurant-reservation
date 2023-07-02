package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RequestDeletingRestaurant {
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long restaurantId;
        private String deleteReqAt;
        public static Response fromDto(RestaurantDto dto) {
            return Response.builder()
                    .restaurantId(dto.getId())
                    .deleteReqAt(dto.getDeleteReqAt().toString())
                    .build();
        }
    }
}
