package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    private Long id;
    private String name;
    private String address;
    private String description;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Integer countOfTables;
    private Integer maxPerReservation;
    private String contactNumber;
    private Double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deleteReqAt;
    private LocalDateTime deletedAt;
    private List<MenuDto> menus;
    private List<ReviewDto> reviews;
    private UserDto manager;

    public static RestaurantDto empty() {
        return new RestaurantDto();
    }

    public static RestaurantDto fromEntity(Restaurant entity) {
        return RestaurantDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddressVO().getAddress())
                .description(entity.getDescription())
                .openTime(entity.getOpen())
                .closeTime(entity.getClose())
                .countOfTables(entity.getCountOfTables())
                .maxPerReservation(entity.getMaxPerReservation())
                .contactNumber(entity.getContactNumber())
                .rating(entity.getRating())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deleteReqAt(entity.getDeleteReqAt())
                .deletedAt(entity.getDeletedAt())
                .menus(entity.getMenus().stream().map(MenuDto::fromEntity).toList())
                .reviews(entity.getReviews().stream().map(ReviewDto::fromEntity).toList())
                .manager(
                        entity.getManager() != null ?
                                UserDto.fromEntity(entity.getManager()) :
                                null
                )
                .build();
    }
}
