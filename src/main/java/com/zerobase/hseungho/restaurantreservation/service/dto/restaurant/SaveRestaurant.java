package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

public class SaveRestaurant {
    @Getter
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
        private List<SaveMenu.Request> menus;
        @NonNull
        private SaveRestaurant.TimeRequest.Request openTime;
        @NonNull
        private SaveRestaurant.TimeRequest.Request closeTime;
        @Min(1)
        private Integer countOfTables;
        private Integer maxPerReservation;
        @NotBlank
        private String contactNumber;
        public void setCoordinate(Double x, Double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class TimeRequest {
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Request {
            @Min(0)
            @Max(23)
            private Integer hour;
            @Min(0)
            @Max(59)
            private Integer minute;
        }
    }

    public static class SaveMenu {
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Request {
            private String name;
            private Long price;
        }
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Response {
            private Long id;
            private String name;
            private Long price;
            public static Response fromDto(MenuDto dto) {
                return Response.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .price(dto.getPrice())
                        .build();
            }
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private String address;
        private String description;
        private List<SaveMenu.Response> menus;
        private String openTime;
        private String closeTime;
        private Integer countOfTables;
        private Integer maxPerReservation;
        private String contactNumber;
        private String managerId;
        private String createdAt;

        public static Response fromDto(RestaurantDto dto) {
            return Response.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .address(dto.getAddress())
                    .description(dto.getDescription())
                    .menus(dto.getMenus().stream().map(SaveMenu.Response::fromDto).toList())
                    .openTime(dto.getOpenTime().toString())
                    .closeTime(dto.getCloseTime().toString())
                    .countOfTables(dto.getCountOfTables())
                    .maxPerReservation(dto.getMaxPerReservation())
                    .contactNumber(dto.getContactNumber())
                    .managerId(dto.getManager().getId())
                    .createdAt(dto.getCreatedAt().toString())
                    .build();
        }
    }
}
