package com.zerobase.hseungho.restaurantreservation.service.controller;

import com.zerobase.hseungho.restaurantreservation.service.appservice.RestaurantService;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.FindRestaurant;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SaveRestaurant;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SearchRestaurant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    @PreAuthorize("hasRole('PARTNER')")
    public SaveRestaurant.Response saveRestaurant(@RequestBody @Valid SaveRestaurant.Request request) {
        return SaveRestaurant.Response.fromDto(
                restaurantService.saveRestaurant(request)
        );
    }

    @GetMapping("${service.api.restaurant.search-auto}")
    @ResponseStatus(HttpStatus.OK)
    public List<String> searchAutocomplete(@RequestParam("keyword") String keyword) {
        return restaurantService.searchAutoComplete(keyword);
    }

    @GetMapping("${service.api.restaurant.search-name}")
    @ResponseStatus(HttpStatus.OK)
    public SearchRestaurant.Response searchRestaurantByName(@RequestParam("key") String name,
                                                            @RequestParam("x") String userX,
                                                            @RequestParam("y") String userY,
                                                            @PageableDefault(sort = "rating", direction = Sort.Direction.DESC) Pageable pageable) {
        return SearchRestaurant.Response.fromListDto(
                restaurantService.searchRestaurantByName(name, userX, userY, pageable)
        );
    }

    @GetMapping("${service.api.restaurant.search-address}")
    @ResponseStatus(HttpStatus.OK)
    public SearchRestaurant.Response searchRestaurantByAddress(@RequestParam("key") String address,
                                                               @RequestParam("x") String userX,
                                                               @RequestParam("y") String userY,
                                                               @PageableDefault(sort = "rating", direction = Sort.Direction.DESC) Pageable pageable) {
        return SearchRestaurant.Response.fromListDto(
                restaurantService.searchRestaurantByAddress(address, userX, userY, pageable)
        );
    }

    @GetMapping("${service.api.restaurant.find}")
    @ResponseStatus(HttpStatus.OK)
    public FindRestaurant.Response findById(@PathVariable("id") Long id) {
        return FindRestaurant.Response.fromDto(
                restaurantService.findById(id)
        );
    }

}
