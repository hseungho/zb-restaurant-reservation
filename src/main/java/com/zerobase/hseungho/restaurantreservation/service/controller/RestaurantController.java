package com.zerobase.hseungho.restaurantreservation.service.controller;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.service.appservice.RestaurantService;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @PutMapping("${service.api.restaurant.update}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PARTNER')")
    public UpdateRestaurant.Response updateRestaurant(@PathVariable("restaurantId") Long restaurantId,
                                                      @RequestBody @Valid UpdateRestaurant.Request request) {
        return UpdateRestaurant.Response.fromDto(
                restaurantService.updateRestaurant(restaurantId, request)
        );
    }

    @DeleteMapping("${service.api.restaurant.delete}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PARTNER')")
    public DeleteRestaurant.Response deleteRestaurant(@PathVariable("restaurantId") Long restaurantId) {
        return DeleteRestaurant.Response.fromDto(
                restaurantService.deleteRestaurant(restaurantId)
        );
    }

    @PostMapping("${service.api.restaurant.request-delete}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PARTNER')")
    public RequestDeletingRestaurant.Response requestDeletingRestaurant(@PathVariable("restaurantId") Long restaurantId,
                                                                        @RequestParam("date")LocalDate date) {
        return RequestDeletingRestaurant.Response.fromDto(
                restaurantService.requestDeletingRestaurant(restaurantId, date)
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
        validateSortProperty(pageable.getSort());
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
        validateSortProperty(pageable.getSort());
        return SearchRestaurant.Response.fromListDto(
                restaurantService.searchRestaurantByAddress(address, userX, userY, pageable)
        );
    }

    @GetMapping("${service.api.restaurant.find}")
    @ResponseStatus(HttpStatus.OK)
    public FindRestaurant.Response findById(@PathVariable("restaurantId") Long restaurantId) {
        return FindRestaurant.Response.fromDto(
                restaurantService.findById(restaurantId)
        );
    }

    @PostMapping("${service.api.restaurant.menu.add}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PARTNER')")
    public AddMenus.Response addMenus(@PathVariable("restaurantId") Long restaurantId,
                                     @RequestBody @Valid AddMenus.Request request) {
        return AddMenus.Response.fromDto(
                restaurantService.addMenus(restaurantId, request)
        );
    }

    @PutMapping("${service.api.restaurant.menu.update}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PARTNER')")
    public UpdateMenu.Response updateMenu(@PathVariable("restaurantId") Long restaurantId,
                                          @PathVariable("menuId") Long menuId,
                                          @RequestBody @Valid UpdateMenu.Request request) {
        return UpdateMenu.Response.fromDto(
                restaurantService.updateMenu(restaurantId, menuId, request)
        );
    }

    @DeleteMapping("${service.api.restaurant.menu.remove}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PARTNER')")
    public RemoveMenu.Response removeMenu(@PathVariable("restaurantId") Long restaurantId,
                                          @PathVariable("menuId") Long menuId) {
        return RemoveMenu.Response.fromDto(
                restaurantService.removeMenu(restaurantId, menuId)
        );
    }

    private void validateSortProperty(Sort sort) {
        String property = sort.iterator().next().getProperty().toLowerCase();
        if ("rating".equals(property) || "name".equals(property) || "distance".equals(property)) {
            return;
        }
        throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SEARCH_RESTAURANT_INVALID_SORT_PROPERTY);
    }

}
