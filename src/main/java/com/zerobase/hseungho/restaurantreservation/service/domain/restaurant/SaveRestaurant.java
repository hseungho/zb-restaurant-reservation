package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

public class SaveRestaurant {
    @Getter
    @Setter
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
        private SaveRestaurantTime.Request openTime;
        @NonNull
        private SaveRestaurantTime.Request closeTime;
        @Min(1)
        private Integer countOfTables;
        private Integer maxPerReservation;
        @NotBlank
        private String contactNumber;
    }

    public static class SaveRestaurantTime {
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
    }
}
