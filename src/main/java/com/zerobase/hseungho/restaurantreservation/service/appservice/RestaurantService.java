package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SaveRestaurant;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SearchAutocomplete;

import java.util.List;

public interface RestaurantService {

    RestaurantDto saveRestaurant(SaveRestaurant.Request request);

    List<SearchAutocomplete.Response> searchAutoComplete(String keyword);

}
