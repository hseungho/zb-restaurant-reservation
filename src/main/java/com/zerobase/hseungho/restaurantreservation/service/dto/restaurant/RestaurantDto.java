package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    private Long id;
    private String name;
    private String address;
    private String description;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private Integer countOfTables;
    private Integer maxPerReservation;
    private String contactNumber;
    private Double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deleteReqAt;
    private LocalDateTime deletedAt;

}
