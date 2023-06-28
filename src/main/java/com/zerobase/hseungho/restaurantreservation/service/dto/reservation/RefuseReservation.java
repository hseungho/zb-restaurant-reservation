package com.zerobase.hseungho.restaurantreservation.service.dto.reservation;

import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RefuseReservation {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private RestaurantResponse restaurant;
        private ReservationResponse reservation;
        public static Response fromDto(ReservationDto dto) {
            return Response.builder()
                    .restaurant(RestaurantResponse.fromDto(dto.getRestaurant()))
                    .reservation(ReservationResponse.fromDto(dto))
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
            private String contactNumber;
            private static RestaurantResponse fromDto(RestaurantDto dto) {
                return RestaurantResponse.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .address(dto.getAddress())
                        .contactNumber(dto.getContactNumber())
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
            private String reservedAt;
            private String refusedAt;
            private Integer numOfPerson;
            private String clientContactNumber;
            private String status;
            private String createdAt;
            private static ReservationResponse fromDto(ReservationDto dto) {
                return ReservationResponse.builder()
                        .id(dto.getId())
                        .number(dto.getNumber())
                        .reservedAt(dto.getReservedAt().toString())
                        .refusedAt(dto.getRefusedAt().toString())
                        .numOfPerson(dto.getNumOfPerson())
                        .clientContactNumber(dto.getClientContactNumber())
                        .status(dto.getStatus().name())
                        .createdAt(dto.getCreatedAt().toString())
                        .build();
            }
        }
    }
}
