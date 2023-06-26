package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.SaveRestaurant;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;

public interface RestaurantService {

    RestaurantDto save(SaveRestaurant.Request request);

}
