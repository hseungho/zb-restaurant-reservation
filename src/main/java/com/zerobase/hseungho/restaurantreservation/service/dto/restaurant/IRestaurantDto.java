package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Menu;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Review;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface IRestaurantDto {
    Long getId();
    String getName();
    String getAddress();
    Double getDistance();
    String getDescription();
    Integer getOpenHour();
    Integer getOpenMinute();
    Integer getCloseHour();
    Integer getCloseMinute();
    Integer getCountOfTables();
    Integer getMaxPerReservation();
    String getContactNumber();
    Double getRating();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getDeleteReqAt();
    LocalDateTime getDeletedAt();
    List<Menu> getMenus();
    List<Review> getReviews();
    User getManager();

}
