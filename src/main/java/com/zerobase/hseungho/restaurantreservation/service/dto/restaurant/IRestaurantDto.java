package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Menu;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Review;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface IRestaurantDto {
    Long getId();
    String getName();
    String getAddress();
    Integer getDistance();
    String getDescription();
    LocalTime getOpen();
    LocalTime getClose();
    int getCount_of_tables();
    int getMax_per_reservation();
    String getContact_number();
    double getRating();
    LocalDateTime getCreated_at();
    LocalDateTime getUpdated_at();
    LocalDateTime getDeleteReqAt();
    LocalDateTime getDeletedAt();
    List<Menu> getMenus();
    List<Review> getReviews();
    User getManager();

}
