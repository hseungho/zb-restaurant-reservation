package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class SaveRestaurant {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String name;
        private String address;
        private String description;
        private List<SaveMenu.Request> menus;
        private LocalDateTime openTime;
        private LocalDateTime closeTime;
        private Integer countOfTables;
        private Integer maxPerReservation;
        private String contactNumber;
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
    }
}
