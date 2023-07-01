package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.NotFoundException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.security.SecurityHolder;
import com.zerobase.hseungho.restaurantreservation.global.util.PageUtils;
import com.zerobase.hseungho.restaurantreservation.global.util.ValidUtils;
import com.zerobase.hseungho.restaurantreservation.global.adapter.webclient.KakaoWebClientComponent;
import com.zerobase.hseungho.restaurantreservation.global.adapter.webclient.dto.CoordinateDto;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Menu;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.IRestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SaveRestaurant;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final Trie<String, String> trie;

    private final KakaoWebClientComponent kakaoWebClientComponent;

    @Override
    public RestaurantDto saveRestaurant(SaveRestaurant.Request request) {
        Restaurant restaurant = this.saveRestaurantEntity(request);
        this.trie.put(restaurant.getName(), restaurant.getName());
        return RestaurantDto.fromEntity(restaurant);
    }

    @Override
    public List<String> searchAutoComplete(String keyword) {
        if (!StringUtils.hasText(keyword)) return List.of();

        return this.trie.prefixMap(keyword).keySet().stream()
                .sorted()
                .limit(10)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<IRestaurantDto> searchRestaurantByName(String name, String userX, String userY, Pageable pageable) {
        CoordinateDto coordinate = validateSearchRestaurantRequest(userX, userY);
        return restaurantRepository.findByNameWithDistance(name, coordinate.getX(), coordinate.getY(), PageUtils.of(pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<IRestaurantDto> searchRestaurantByAddress(String address, String userX, String userY, Pageable pageable) {
        CoordinateDto coordinate = validateSearchRestaurantRequest(userX, userY);
        return restaurantRepository.findByAddressWithDistance(address, coordinate.getX(), coordinate.getY(), PageUtils.of(pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantDto findById(Long id) {
        return RestaurantDto.fromEntity(
                restaurantRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT))
        );
    }

    private CoordinateDto validateSearchRestaurantRequest(String userX, String userY) {
        try {
            double x = Double.parseDouble(userX);
            double y = Double.parseDouble(userY);
            return new CoordinateDto(x, y);
        } catch (NumberFormatException e) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SEARCH_RESTAURANT_INVALID_VALUE);
        }
    }

    @Transactional
    protected Restaurant saveRestaurantEntity(SaveRestaurant.Request request) {
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));

        validateSaveRestaurantRequest(user, request);

        CoordinateDto coordinate = kakaoWebClientComponent.getCoordinateByAddress(request.getAddress());
        request.setCoordinate(coordinate.getX(), coordinate.getY());

        Restaurant restaurant = Restaurant.create(request, user);
        addMenusIfPresent(restaurant, request);

        return restaurantRepository.save(restaurant);
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
        if (restaurantRepository.existsByManager(user)) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_RESTAURANT_ALREADY_MANAGER);
        }
    }
}
