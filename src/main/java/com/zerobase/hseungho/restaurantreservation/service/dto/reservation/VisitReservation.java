package com.zerobase.hseungho.restaurantreservation.service.dto.reservation;

import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class VisitReservation {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private RestaurantResponse restaurant;
        private ReservationResponse reservation;
        private UserResponse client;
        public static Response fromDto(ReservationDto dto) {
            return Response.builder()
                    .restaurant(RestaurantResponse.fromDto(dto.getRestaurant()))
                    .reservation(ReservationResponse.fromDto(dto))
                    .client(UserResponse.fromDto(dto.getClient()))
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
            private Integer numOfPerson;
            private String clientContactNumber;
            private String reservedAt;
            private String approvedAt;
            private String visitedAt;
            private String createdAt;
            private String status;
            private static ReservationResponse fromDto(ReservationDto dto) {
                return ReservationResponse.builder()
                        .id(dto.getId())
                        .number(dto.getNumber())
                        .numOfPerson(dto.getNumOfPerson())
                        .clientContactNumber(dto.getClientContactNumber())
                        .reservedAt(dto.getReservedAt().toString())
                        .approvedAt(dto.getApprovedAt().toString())
                        .visitedAt(dto.getVisitedAt().toString())
                        .createdAt(dto.getCreatedAt().toString())
                        .status(dto.getStatus().name())
                        .build();
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        private static class UserResponse {
            private String nickname;
            private static UserResponse fromDto(UserDto dto) {
                return UserResponse.builder()
                        .nickname(dto.getNickname())
                        .build();
            }
        }
    }
}
