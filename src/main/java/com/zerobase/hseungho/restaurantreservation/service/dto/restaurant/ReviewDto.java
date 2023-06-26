package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long id;
    private Double rating;
    private String content;
    private String imageSrc;
    private UserDto author;
    private RestaurantDto restaurant;
}
