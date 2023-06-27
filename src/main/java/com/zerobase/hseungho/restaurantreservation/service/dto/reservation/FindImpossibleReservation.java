package com.zerobase.hseungho.restaurantreservation.service.dto.reservation;

import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantTimeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class FindImpossibleReservation {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private RestaurantResponse restaurant;
        private List<String> impossibleReservationTimes;
        public static Response fromDto(ReservationDtoWithImpossibleTimes dto) {
            return Response.builder()
                    .restaurant(RestaurantResponse.fromDto(dto.getRestaurant()))
                    .impossibleReservationTimes(
                            dto.getImpossibleReservationTimes().stream()
                                    .map(LocalDateTime::toString)
                                    .toList()
                    )
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
            private RestaurantTimeResponse openTime;
            private RestaurantTimeResponse closeTime;
            private Integer maxPerReservation;
            private String contactNumber;
            private Double rating;
            public static RestaurantResponse fromDto(RestaurantDto dto) {
                return RestaurantResponse.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .address(dto.getAddress())
                        .openTime(RestaurantTimeResponse.fromDto(dto.getOpenTime()))
                        .closeTime(RestaurantTimeResponse.fromDto(dto.getCloseTime()))
                        .maxPerReservation(dto.getMaxPerReservation())
                        .contactNumber(dto.getContactNumber())
                        .rating(dto.getRating())
                        .build();
            }

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder
            private static class RestaurantTimeResponse {
                private Integer hour;
                private Integer minute;
                private static RestaurantTimeResponse fromDto(RestaurantTimeDto dto) {
                    return RestaurantTimeResponse.builder()
                            .hour(dto.getHour())
                            .minute(dto.getMinute())
                            .build();
                }
            }
        }
    }
}
