package com.zerobase.hseungho.restaurantreservation.service.controller;

import com.zerobase.hseungho.restaurantreservation.service.appservice.RestaurantService;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SaveRestaurant;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SearchAutocomplete;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${service.api.prefix}")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("${service.api.restaurant.save}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    public SaveRestaurant.Response saveRestaurant(@RequestBody @Valid SaveRestaurant.Request request) {
        return SaveRestaurant.Response.fromDto(
                restaurantService.saveRestaurant(request)
        );
    }

    @GetMapping("${service.api.restaurant.search-auto}")
    @ResponseStatus(HttpStatus.OK)
    public List<SearchAutocomplete.Response> searchAutocomplete(@RequestParam("keyword") String keyword) {
        return restaurantService.searchAutoComplete(keyword);
    }

}
