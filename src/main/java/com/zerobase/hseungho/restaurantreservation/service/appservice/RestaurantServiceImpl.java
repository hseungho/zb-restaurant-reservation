package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.SaveRestaurant;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    @Override
    public RestaurantDto save(SaveRestaurant.Request request) {
        return null;
    }
}
