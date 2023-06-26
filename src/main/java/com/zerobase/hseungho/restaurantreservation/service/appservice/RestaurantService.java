package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SaveRestaurant;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;

public interface RestaurantService {

    RestaurantDto saveRestaurant(SaveRestaurant.Request request);

}
