package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class AddMenus {
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        private List<MenuRequest> menus;

        @Data @NoArgsConstructor @AllArgsConstructor @Builder
        public static class MenuRequest {
            private String name;
            private Long price;
        }
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private RestaurantResponse restaurant;
        private List<MenuResponse> menus;
        public static Response fromDto(RestaurantDto dto) {
            return Response.builder()
                    .restaurant(RestaurantResponse.fromDto(dto))
                    .menus(dto.getMenus().stream().map(MenuResponse::fromDto).toList())
                    .build();
        }
        @Data @NoArgsConstructor @AllArgsConstructor @Builder
        private static class RestaurantResponse {
            private Long id;
            private String name;
            private String address;
            private String description;
            private String openTime;
            private String closeTime;
            private Integer countOfTables;
            private Integer maxPerReservation;
            private String contactNumber;
            private String createdAt;
            private static RestaurantResponse fromDto(RestaurantDto dto) {
                return RestaurantResponse.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .address(dto.getAddress())
                        .description(dto.getDescription())
                        .openTime(dto.getOpenTime().toString())
                        .closeTime(dto.getCloseTime().toString())
                        .countOfTables(dto.getCountOfTables())
                        .maxPerReservation(dto.getMaxPerReservation())
                        .contactNumber(dto.getContactNumber())
                        .createdAt(dto.getCreatedAt().toString())
                        .build();
            }
        }

        @Data @NoArgsConstructor @AllArgsConstructor @Builder
        private static class MenuResponse {
            private Long id;
            private String name;
            private long price;
            private static MenuResponse fromDto(MenuDto dto) {
                return MenuResponse.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .price(dto.getPrice())
                        .build();
            }
        }
    }
}
