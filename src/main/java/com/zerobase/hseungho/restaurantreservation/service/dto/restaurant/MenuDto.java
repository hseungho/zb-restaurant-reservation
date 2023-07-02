package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {
    private Long id;
    private String name;
    private Long price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private RestaurantDto restaurant;

    public static MenuDto fromEntity(Menu entity) {
        return MenuDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .restaurant(RestaurantDto.fromEntity(entity.getRestaurant()))
                .build();
    }
}
