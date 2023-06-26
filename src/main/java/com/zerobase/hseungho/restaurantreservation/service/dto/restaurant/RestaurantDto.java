package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private RestaurantTimeDto openTime;
    private RestaurantTimeDto closeTime;
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

    public static RestaurantDto fromEntity(Restaurant entity) {
        return RestaurantDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddressVO().getAddress())
                .description(entity.getDescription())
                .openTime(RestaurantTimeDto.of(
                        entity.getRestaurantTimeVO().getOpenHour(), entity.getRestaurantTimeVO().getOpenMinute()))
                .closeTime(RestaurantTimeDto.of(
                        entity.getRestaurantTimeVO().getCloseHour(), entity.getRestaurantTimeVO().getCloseMinute()))
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
                .manager(UserDto.fromEntity(entity.getManager()))
                .build();
    }
}
