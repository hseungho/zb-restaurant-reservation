package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.adapter.fileupload.AwsS3ImageManager;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.ForbiddenException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.NotFoundException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.security.SecurityHolder;
import com.zerobase.hseungho.restaurantreservation.global.util.PageUtils;
import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import com.zerobase.hseungho.restaurantreservation.global.util.ValidUtils;
import com.zerobase.hseungho.restaurantreservation.global.adapter.webclient.KakaoWebClientComponent;
import com.zerobase.hseungho.restaurantreservation.global.adapter.webclient.dto.CoordinateDto;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Menu;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Review;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.*;
import com.zerobase.hseungho.restaurantreservation.service.repository.*;
import com.zerobase.hseungho.restaurantreservation.service.type.ReservationStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;
    private final MenuRepository menuRepository;

    private final Trie<String, String> trie;

    private final KakaoWebClientComponent kakaoWebClientComponent;
    private final AwsS3ImageManager imageManager;

    /**
     * 서버 Boot 시, DB에 저장되어 있는 모든 매장 정보를 조회하여, <br>
     * 자동완성을 위한 Trie 객체에 매장 이름을 저장하는 PostConstruct 메소드.
     */
    @PostConstruct
    private void initTrie() {
        List<Restaurant> restaurants = this.restaurantRepository.findAll();
        restaurants.forEach(it -> this.trie.put(it.getName(), it.getName()));
    }

    @Override
    public RestaurantDto saveRestaurant(SaveRestaurant.Request request) {
        Restaurant restaurant = this.saveRestaurantEntity(request);
        this.trie.put(restaurant.getName(), restaurant.getName());
        return RestaurantDto.fromEntityWithAssociate(restaurant);
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
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));
        if (restaurant.isDeleted()) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_FIND_RESTAURANT_DELETE_RESTAURANT);
        }
        return RestaurantDto.fromEntityWithAssociate(restaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantDto findByPartner() {
        Restaurant restaurant = restaurantRepository.findByManager(SecurityHolder.getUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));
        if (restaurant.isDeleted()) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_FIND_RESTAURANT_DELETE_RESTAURANT);
        }
        return RestaurantDto.fromEntityWithAssociate(restaurant);
    }

    @Override
    @Transactional
    public RestaurantDto updateRestaurant(Long restaurantId, UpdateRestaurant.Request request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));

        validateUpdateRestaurantRequest(SecurityHolder.getUser(), restaurant, request);

        if (request.getAddress().equals(restaurant.getAddressVO().getAddress())) {
            request.setCoordinate(restaurant.getAddressVO().getX(), restaurant.getAddressVO().getY());
        } else {
            CoordinateDto coordinate = kakaoWebClientComponent.getCoordinateByAddress(request.getAddress());
            request.setCoordinate(coordinate.getX(), coordinate.getY());
        }

        restaurant.update(request);

        return RestaurantDto.fromEntityWithAssociate(restaurant);
    }

    @Override
    @Transactional
    public RestaurantDto deleteRestaurant(Long restaurantId) {
        LocalDateTime now = SeoulDateTime.now();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));

        validateDeleteRestaurantRequest(SecurityHolder.getUser(), restaurant, now);

        List<Review> reviews = restaurant.getReviews();
        if (!CollectionUtils.isEmpty(reviews)) {
            reviews.stream()
                    .filter(it -> it.getImageSrc() != null)
                    .peek(it -> imageManager.delete(it.getImageSrc()))
                    .close();
        }
        restaurant.deleteNow(now);

        return RestaurantDto.fromEntityWithAssociate(restaurant);
    }

    @Override
    @Transactional
    public RestaurantDto requestDeletingRestaurant(Long restaurantId, LocalDate date) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));

        validateRequestDeletingRestaurantRequest(SecurityHolder.getUser(), restaurant, date.atStartOfDay());

        restaurant.requestDeleting(date);

        return RestaurantDto.fromEntityWithAssociate(restaurant);
    }

    @Override
    @Transactional
    public RestaurantDto addMenus(Long restaurantId, AddMenus.Request request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));

        validateAddMenusRequest(SecurityHolder.getUser(), restaurant, request);

        for (AddMenus.Request.MenuRequest menuRequest : request.getMenus()) {
            restaurant.addMenu(Menu.create(menuRequest.getName(), menuRequest.getPrice()));
        }

        // JPA Cascade 를 통해 새 메뉴를 저장되기에 saveAndFlush 호출이 필요하진 않지만,
        // 응답값으로 생성된 메뉴의 ID를 반환하기 위해서 DTO 생성 이전에 DB의 Auto_increment ID가 선 요구되기에
        // 아래의 saveAndFlush 호출을 통해 쿼리를 즉시 날리고 영속성을 갱신하도록 하였다.
        restaurantRepository.saveAndFlush(restaurant);

        return RestaurantDto.fromEntityWithAssociate(restaurant);
    }

    @Override
    @Transactional
    public RestaurantDto updateMenu(Long restaurantId, Long menuId, UpdateMenu.Request request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));

        validateUpdateMenuRequest(SecurityHolder.getUser(), restaurant, request);

        restaurant.updateMenu(menuId, request.getName(), request.getPrice());

        return RestaurantDto.fromEntityWithAssociate(restaurant);
    }

    @Override
    @Transactional
    public RestaurantDto removeMenu(Long restaurantId, Long menuId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));

        Menu menu = menuRepository.findByIdAndRestaurant(menuId, restaurant)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_MENU));

        validateRemoveMenuRequest(SecurityHolder.getUser(), restaurant);

        restaurant.removeMenu(menu);

        return RestaurantDto.fromEntityWithAssociate(restaurant);
    }

    private void validateRemoveMenuRequest(User user, Restaurant restaurant) {
        if (!restaurant.isManager(user)) {
            // 해당 매장의 점장이 아닙니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_REMOVE_MENU_NOY_YOUR_RESTAURANT);
        }
        if (restaurant.isDeleted()) {
            // 영업 종료된 매장의 메뉴를 삭제할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_REMOVE_MENU_DELETE_RESTAURANT);
        }
    }

    private void validateUpdateMenuRequest(User user, Restaurant restaurant, UpdateMenu.Request request) {
        if (!ValidUtils.hasTexts(request.getName()) || ValidUtils.isLessThan(0, request.getPrice())) {
            // 메뉴 수정에 필요한 모든 정보를 입력해주세요.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_UPDATE_MENU_BLANK);
        }
        if (!restaurant.isManager(user)) {
            // 해당 매장의 점장이 아닙니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_UPDATE_MENU_NOT_YOUR_RESTAURANT);
        }
        if (restaurant.isDeleted()) {
            // 영업 종료된 매장의 메뉴를 수정할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_UPDATE_MENU_DELETE_RESTAURANT);
        }
    }

    private void validateAddMenusRequest(User user, Restaurant restaurant, AddMenus.Request request) {
        if (CollectionUtils.isEmpty(request.getMenus())
            || !ValidUtils.hasTexts(request.getMenus().stream()
                    .map(AddMenus.Request.MenuRequest::getName).toArray(String[]::new))
            || ValidUtils.isLessThan(0L, request.getMenus().stream()
                    .mapToLong(AddMenus.Request.MenuRequest::getPrice).toArray())) {
            // 메뉴 추가를 위해 필요한 모든 정보를 입력해주세요.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_ADD_MENUS_BLANK);
        }
        if (!restaurant.isManager(user)) {
            // 해당 매장의 점장이 아닙니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_ADD_MENUS_NOT_YOUR_RESTAURANT);
        }
        if (restaurant.isDeleted()) {
            // 영업 종료된 매장의 메뉴를 추가할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_ADD_MENUS_DELETE_RESTAURANT);
        }
    }

    private void validateRequestDeletingRestaurantRequest(User user, Restaurant restaurant, LocalDateTime date) {
        if (restaurant.isDeleted()) {
            // 이미 삭제된 매장입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_REQUEST_DELETING_RESTAURANT_ALREADY);
        }
        if (date.isBefore(SeoulDateTime.now())) {
            // 현재보다 이전 시간에 매장을 삭제 요청할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_REQUEST_DELETING_RESTAURANT_REQ_TIME_IS_BEFORE_NOW);
        }
        if (!restaurant.isManager(user)) {
            // 해당 매장의 점장이 아닙니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_REQUEST_DELETING_RESTAURANT_NOT_YOUR_RESTAURANT);
        }
        if (reservationRepository.existsByRestaurantAndStatusAndReservedAtGreaterThanEqual(restaurant, ReservationStatus.RESERVED, date)) {
            // 해당 매장에 예약이 남아있어서 해당 일자에 매장을 삭제할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_REQUEST_DELETING_RESTAURANT_REMAIN_RESERVATION);
        }
    }

    private void validateDeleteRestaurantRequest(User user, Restaurant restaurant, LocalDateTime now) {
        if (restaurant.isDeleted()) {
            // 이미 영업종료된 매장입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_DELETE_RESTAURANT_ALREADY);
        }
        if (!restaurant.isManager(user)) {
            // 해당 매장의 점장이 아닙니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_DELETE_RESTAURANT_NOT_YOUR_RESTAURANT);
        }
        if (reservationRepository.existsByRestaurantAndStatusAndReservedAtGreaterThanEqual(restaurant, ReservationStatus.RESERVED, now)) {
            // 해당 매장에 예약이 남아있어서 매장을 삭제할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_DELETE_RESTAURANT_REMAIN_RESERVATION);
        }
    }

    private void validateUpdateRestaurantRequest(User user, Restaurant restaurant, UpdateRestaurant.Request request) {
        if (!ValidUtils.hasTexts(request.getName(), request.getAddress(), request.getDescription(), request.getContactNumber())
            || !ValidUtils.isExactHour(request.getOpenTime().getHour(), request.getCloseTime().getHour())
            || !ValidUtils.isExactMinute(request.getOpenTime().getMinute(), request.getCloseTime().getMinute())
            || ValidUtils.isLessThan(1, request.getCountOfTables())) {
            // 정보 수정의 파라미터를 올바르게 요청해주세요.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_UPDATE_RESTAURANT_BLANK);
        }
        if (!restaurant.isManager(user)) {
            // 해당 매장의 점장이 아닙니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_UPDATE_RESTAURANT_NOT_YOUR_RESTAURANT);
        }
    }

    private CoordinateDto validateSearchRestaurantRequest(String userX, String userY) {
        try {
            double x = Double.parseDouble(userX);
            double y = Double.parseDouble(userY);
            return new CoordinateDto(x, y);
        } catch (NullPointerException | NumberFormatException e) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SEARCH_RESTAURANT_INVALID_VALUE);
        }
    }

    /**
     * 매장 정보를 DB에 저장하는 메소드이다. <br>
     * 요청 정보를 검증하고, kakao API로 해당 주소의 좌표를 요청 정보에 추가한 뒤, <br>
     * 요청 정보를 이용하여 매장 인스턴스를 초기화하여 DB에 저장한다. <br>
     * <br>
     * 매장 정보 중 x 좌표와 y 좌표의 값이 null이 되면 안되기 때문에, kakao API 호출에 대한 로직도 <br>
     * 하나의 트랜잭션으로 여긴다. <br>
     * <br>
     * @param request 매장 정보 저장 요청 정보
     * @return 매장 Entity 클래스
     */
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
                || ValidUtils.isLessThan(1, request.getCountOfTables())) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_RESTAURANT_BLANK);
        }
        if (restaurantRepository.existsByManager(user)) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_RESTAURANT_ALREADY_MANAGER);
        }
    }
}
