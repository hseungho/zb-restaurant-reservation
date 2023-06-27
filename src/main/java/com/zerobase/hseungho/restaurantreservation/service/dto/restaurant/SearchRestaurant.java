package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class SearchRestaurant {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private List<ResponseDocument> documents;

        public static Response fromListDto(List<IRestaurantDto> dtos) {
            return Response.builder()
                    .documents(dtos.stream().map(ResponseDocument::fromDto).toList())
                    .build();
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class ResponseDocument {
            private Long restaurantId;
            private String name;
            private String address;
            private Double distance;
            private RestaurantTimeDto openTime;
            private RestaurantTimeDto closeTime;
            private Integer maxPerReservation;
            private String contactNumber;

            public static ResponseDocument fromDto(IRestaurantDto dto) {
                return ResponseDocument.builder()
                        .restaurantId(dto.getId())
                        .name(dto.getName())
                        .address(dto.getAddress())
                        .distance(dto.getDistance())
                        .openTime(RestaurantTimeDto.of(dto.getOpenHour(), dto.getOpenMinute()))
                        .closeTime(RestaurantTimeDto.of(dto.getCloseHour(), dto.getCloseMinute()))
                        .maxPerReservation(dto.getMaxPerReservation())
                        .contactNumber(dto.getContactNumber())
                        .build();
            }
        }
    }
}
