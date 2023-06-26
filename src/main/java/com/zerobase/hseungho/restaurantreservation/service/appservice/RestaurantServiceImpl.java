package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.security.SecurityHolder;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.SaveRestaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public RestaurantDto save(SaveRestaurant.Request request) {
        User user = SecurityHolder.getUser();

        validateSaveRestaurantRequest(user, request);

        return null;
    }

    private void validateSaveRestaurantRequest(User user, SaveRestaurant.Request request) {
        if (!user.isPartner()) {
            // cannot customer save restaurant
        }
        if (restaurantRepository.existsByManager(user)) {
            // already have restaurant
        }
    }
}
