package com.zerobase.hseungho.restaurantreservation.service.controller;

import com.zerobase.hseungho.restaurantreservation.service.appservice.RestaurantService;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SaveRestaurant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${service.api.prefix}")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("${service.api.restaurant.save}")
    @ResponseStatus(HttpStatus.CREATED)
    public SaveRestaurant.Response saveRestaurant(@RequestBody @Valid SaveRestaurant.Request request) {
        return SaveRestaurant.Response.fromDto(
                restaurantService.save(request)
        );
    }

}
