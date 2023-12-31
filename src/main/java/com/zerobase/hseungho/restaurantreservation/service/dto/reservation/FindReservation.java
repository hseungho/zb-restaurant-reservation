package com.zerobase.hseungho.restaurantreservation.service.dto.reservation;

import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.UserDto;
import com.zerobase.hseungho.restaurantreservation.service.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FindReservation {
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
            private String description;
            private String contactNumber;
            private String openTime;
            private String closeTime;
            private static RestaurantResponse fromDto(RestaurantDto dto) {
                return RestaurantResponse.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .address(dto.getAddress())
                        .description(dto.getDescription())
                        .contactNumber(dto.getContactNumber())
                        .openTime(dto.getOpenTime().toString())
                        .closeTime(dto.getCloseTime().toString())
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
            private String canceledAt;
            private String approvedAt;
            private String refusedAt;
            private String visitedAt;
            private Integer numOfPerson;
            private String clientContactNumber;
            private String status;
            private String createdAt;
            private static ReservationResponse fromDto(ReservationDto dto) {
                return ReservationResponse.builder()
                        .id(dto.getId())
                        .number(dto.getNumber())
                        .reservedAt(dto.getReservedAt().toString())
                        .canceledAt(dto.getStatus() == ReservationStatus.CANCELED ?
                                dto.getCanceledAt().toString() : null)
                        .approvedAt(dto.getStatus() == ReservationStatus.APPROVED || dto.getStatus() == ReservationStatus.VISITED ?
                                dto.getApprovedAt().toString() : null)
                        .refusedAt(dto.getStatus() == ReservationStatus.REFUSED ?
                                dto.getRefusedAt().toString() : null)
                        .visitedAt(dto.getStatus() == ReservationStatus.VISITED ?
                                dto.getVisitedAt().toString() : null)
                        .numOfPerson(dto.getNumOfPerson())
                        .clientContactNumber(dto.getClientContactNumber())
                        .status(dto.getStatus().name())
                        .createdAt(dto.getCreatedAt().toString())
                        .build();
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        private static class UserResponse {
            private String id;
            private String nickname;
            private String type;
            private static UserResponse fromDto(UserDto dto) {
                return UserResponse.builder()
                        .id(dto.getId())
                        .nickname(dto.getNickname())
                        .type(dto.getType().name())
                        .build();
            }
        }
    }
}
