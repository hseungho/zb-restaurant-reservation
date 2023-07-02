package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UpdateMenu {
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        private String name;
        private Long price;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private RestaurantResponse restaurant;
        private MenuResponse menu;
        public static Response fromDto(MenuDto dto) {
            return Response.builder()
                    .menu(MenuResponse.fromDto(dto))
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
