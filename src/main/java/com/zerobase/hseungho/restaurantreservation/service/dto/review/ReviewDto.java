package com.zerobase.hseungho.restaurantreservation.service.dto.review;

import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Review;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReservationDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
    private ReservationDto reservation;

    public static ReviewDto fromEntity(Review entity) {
        return ReviewDto.builder()
                .id(entity.getId())
                .rating(entity.getRating())
                .content(entity.getContent())
                .imageSrc(entity.getImageSrc())
                .author(UserDto.fromEntity(entity.getAuthor()))
                .restaurant(RestaurantDto.fromEntityWithAssociate(entity.getRestaurant()))
                .reservation(ReservationDto.fromEntity(entity.getReservation()))
                .build();
    }
}
