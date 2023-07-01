package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.dto.user.UserDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

public class UpdateRestaurant {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank
        private String name;
        @NotBlank
        private String address;
        private Double x;
        private Double y;
        private String description;
        @NonNull
        private UpdateRestaurant.Request.TimeRequest openTime;
        @NonNull
        private UpdateRestaurant.Request.TimeRequest closeTime;
        @Min(1)
        private Integer countOfTables;
        private Integer maxPerReservation;
        @NotBlank
        private String contactNumber;
        public void setCoordinate(Double x, Double y) {
            this.x = x;
            this.y = y;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class TimeRequest {
            private Integer hour;
            private Integer minute;
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private RestaurantResponse restaurant;
        private UserResponse manager;
        public static Response fromDto(RestaurantDto dto) {
            return Response.builder()
                    .restaurant(RestaurantResponse.fromDto(dto))
                    .manager(UserResponse.fromDto(dto.getManager()))
                    .build();
        }

        @Data @NoArgsConstructor @AllArgsConstructor @Builder
        private static class RestaurantResponse {
            private Long id;
            private String name;
            private String address;
            private String description;
            private List<MenuResponse> menus;
            private RestaurantTimeResponse openTime;
            private RestaurantTimeResponse closeTime;
            private int countOfTables;
            private int maxPerReservation;
            private String contactNumber;
            private String createdAt;
            private String updatedAt;
            private static RestaurantResponse fromDto(RestaurantDto dto) {
                return RestaurantResponse.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .address(dto.getAddress())
                        .description(dto.getDescription())
                        .menus(dto.getMenus().stream().map(MenuResponse::fromDto).toList())
                        .openTime(RestaurantTimeResponse.of(dto.getOpenTime()))
                        .closeTime(RestaurantTimeResponse.of(dto.getCloseTime()))
                        .countOfTables(dto.getCountOfTables())
                        .maxPerReservation(dto.getMaxPerReservation())
                        .contactNumber(dto.getContactNumber())
                        .createdAt(dto.getCreatedAt().toString())
                        .updatedAt(dto.getUpdatedAt().toString())
                        .build();
            }

            @Data @NoArgsConstructor @AllArgsConstructor @Builder
            private static class MenuResponse {
                private Long id;
                private String name;
                private Long price;
                private static MenuResponse fromDto(MenuDto dto) {
                    return MenuResponse.builder()
                            .id(dto.getId())
                            .name(dto.getName())
                            .price(dto.getPrice())
                            .build();
                }
            }

            @Data @NoArgsConstructor @AllArgsConstructor @Builder
            private static class RestaurantTimeResponse {
                private int hour;
                private int minute;
                private static RestaurantTimeResponse of(LocalTime time) {
                    return RestaurantTimeResponse.builder()
                            .hour(time.getHour())
                            .minute(time.getMinute())
                            .build();
                }
            }
        }

        @Data @NoArgsConstructor @AllArgsConstructor @Builder
        private static class UserResponse {
            private String id;
            private String nickname;
            private String userType;
            private static UserResponse fromDto(UserDto dto) {
                return UserResponse.builder()
                        .id(dto.getId())
                        .nickname(dto.getNickname())
                        .userType(dto.getType().name())
                        .build();
            }
        }
    }
}
