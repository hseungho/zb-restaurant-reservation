package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
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
        private LocalDateTime openTime;
        @NonNull
        private LocalDateTime closeTime;
        @Min(1)
        private Integer countOfTables;
        private Integer maxPerReservation;
        @NotBlank
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
