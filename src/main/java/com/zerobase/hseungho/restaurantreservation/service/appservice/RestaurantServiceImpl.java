package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.security.SecurityHolder;
import com.zerobase.hseungho.restaurantreservation.global.util.ValidUtils;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Menu;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SaveRestaurant;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SearchAutocomplete;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Trie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final Trie<String, String> trie;

    @Override
    @Transactional
    public RestaurantDto saveRestaurant(SaveRestaurant.Request request) {
        User user = SecurityHolder.getUser();

        validateSaveRestaurantRequest(user, request);

        Restaurant restaurant = Restaurant.create(request, user);
        addMenusIfPresent(restaurant, request);

        return RestaurantDto.fromEntity(
                restaurantRepository.save(restaurant)
        );
    }

    @Override
    public List<SearchAutocomplete.Response> searchAutoComplete(String keyword) {
        return null;
    }

    private void addMenusIfPresent(Restaurant restaurant, SaveRestaurant.Request request) {
        if (CollectionUtils.isEmpty(request.getMenus())) return;

        request.getMenus().forEach(e -> restaurant.addMenu(Menu.create(e.getName(), e.getPrice())));
    }

    private void validateSaveRestaurantRequest(User user, SaveRestaurant.Request request) {
        if (!ValidUtils.hasTexts(request.getName(), request.getAddress(), request.getContactNumber())
                || !ValidUtils.isExactHour(request.getOpenTime().getHour(), request.getCloseTime().getHour())
                || !ValidUtils.isExactMinute(request.getOpenTime().getMinute(), request.getCloseTime().getMinute())
                || !ValidUtils.isMin(1, request.getCountOfTables())) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_RESTAURANT_BLANK);
        }
        if (!user.isPartner()) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_RESTAURANT_USER_NOT_PARTNER);
        }
        if (restaurantRepository.existsByManager(user)) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_RESTAURANT_ALREADY_MANAGER);
        }
    }
}
