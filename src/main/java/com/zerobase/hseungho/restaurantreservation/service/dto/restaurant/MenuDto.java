package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {
    private Long id;
    private String name;
    private Long price;
    private RestaurantDto restaurant;
}
