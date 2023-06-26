package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SaveRestaurant;

import java.util.List;

public interface RestaurantService {

    RestaurantDto saveRestaurant(SaveRestaurant.Request request);

    List<String> searchAutoComplete(String keyword);

}
