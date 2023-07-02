package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.adapter.webclient.KakaoWebClientComponent;
import com.zerobase.hseungho.restaurantreservation.global.adapter.webclient.dto.CoordinateDto;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Menu;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.*;
import com.zerobase.hseungho.restaurantreservation.service.repository.MenuRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.ReservationRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import com.zerobase.hseungho.restaurantreservation.util.MockBuilder;
import com.zerobase.hseungho.restaurantreservation.util.TestSecurityHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplUnitTest {

    @InjectMocks
    private RestaurantServiceImpl restaurantService;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private KakaoWebClientComponent kakaoWebClientComponent;

    @Test
    @DisplayName("매장 정보 수정")
    void test_updateRestaurant() {
        // given
        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_PARTNER);
        Restaurant restaurant = MockBuilder.mockRestaurant(user);
        given(restaurantRepository.findById(anyLong()))
                .willReturn(Optional.of(restaurant));
        given(kakaoWebClientComponent.getCoordinateByAddress(anyString()))
                .willReturn(new CoordinateDto(127.123456, 36.123456));
        // when
        RestaurantDto result = restaurantService.updateRestaurant(1L, UpdateRestaurant.Request.builder()
                .name("new restaurant name")
                .address("new address")
                .description("new description")
                .openTime(UpdateRestaurant.Request.TimeRequest.builder()
                        .hour(10)
                        .minute(30)
                        .build())
                .closeTime(UpdateRestaurant.Request.TimeRequest.builder()
                        .hour(22)
                        .minute(45)
                        .build())
                .countOfTables(10)
                .maxPerReservation(8)
                .contactNumber("new contact number")
                .build());
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("new restaurant name", result.getName());
        Assertions.assertEquals("new address", result.getAddress());
        Assertions.assertEquals("new description", result.getDescription());
        Assertions.assertEquals(10, result.getOpenTime().getHour());
        Assertions.assertEquals(30, result.getOpenTime().getMinute());
        Assertions.assertEquals(22, result.getCloseTime().getHour());
        Assertions.assertEquals(45, result.getCloseTime().getMinute());
        Assertions.assertEquals(10, result.getCountOfTables());
        Assertions.assertEquals(8, result.getMaxPerReservation());
        Assertions.assertEquals(user.getNickname(), result.getManager().getNickname());
    }

    @Test
    @DisplayName("매장 삭제")
    void test_deleteRestaurant() {
        // given
        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_PARTNER);
        Restaurant restaurant = MockBuilder.mockRestaurant(user);
        given(restaurantRepository.findById(anyLong()))
                .willReturn(Optional.of(restaurant));
        given(reservationRepository.existsByRestaurantAndStatusAndReservedAtGreaterThanEqual(any(), any(), any()))
                .willReturn(false);
        // when
        RestaurantDto result = restaurantService.deleteRestaurant(1L);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(restaurant.getId(), result.getId());
        Assertions.assertNotNull(result.getDeleteReqAt());
        Assertions.assertNotNull(result.getDeletedAt());
        Assertions.assertNull(result.getManager());
    }

    @Test
    @DisplayName("매장 삭제 요청")
    void test_requestDeletingRestaurant() {
        // given
        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_PARTNER);
        Restaurant restaurant = MockBuilder.mockRestaurant(user);
        given(restaurantRepository.findById(anyLong()))
                .willReturn(Optional.of(restaurant));
        given(reservationRepository.existsByRestaurantAndStatusAndReservedAtGreaterThanEqual(any(), any(), any()))
                .willReturn(false);
        // when
        RestaurantDto result = restaurantService.requestDeletingRestaurant(1L, LocalDate.now().plusDays(1));
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(restaurant.getId(), result.getId());
        Assertions.assertNotNull(result.getDeleteReqAt());
        Assertions.assertNotNull(result.getManager());
    }

    @Test
    @DisplayName("메뉴 추가")
    void test_addMenus() {
        // given
        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_PARTNER);
        Restaurant restaurant = MockBuilder.mockRestaurant(user);
        int originMenuSize = restaurant.getMenus().size();
        given(restaurantRepository.findById(anyLong()))
                .willReturn(Optional.of(restaurant));
        // when
        RestaurantDto result = restaurantService.addMenus(1L, AddMenus.Request.builder()
                .menus(new ArrayList<>(List.of(
                        AddMenus.Request.MenuRequest.builder()
                                .name("추가메뉴 1")
                                .price(1000L)
                                .build(),
                        AddMenus.Request.MenuRequest.builder()
                                .name("추가메뉴 2")
                                .price(2000L)
                                .build(),
                        AddMenus.Request.MenuRequest.builder()
                                .name("추가메뉴 3")
                                .price(3000L)
                                .build())))
                .build());
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(restaurant.getId(), result.getId());
        Assertions.assertEquals(restaurant.getName(), result.getName());
        Assertions.assertEquals(restaurant.getDescription(), result.getDescription());
        Assertions.assertEquals(originMenuSize+3, result.getMenus().size());
    }

    @Test
    @DisplayName("메뉴 수정")
    void test_updateMenu() {
        // given
        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_PARTNER);
        Restaurant restaurant = MockBuilder.mockRestaurant(user);
        Menu menu = MockBuilder.mockMenu(restaurant);
        given(restaurantRepository.findById(anyLong()))
                .willReturn(Optional.of(restaurant));
        given(menuRepository.findByIdAndRestaurant(anyLong(), any()))
                .willReturn(Optional.of(menu));
        // when
        MenuDto result = restaurantService.updateMenu(1L, 1L, UpdateMenu.Request.builder()
                .name("추가메뉴")
                .price(10000L)
                .build()
        );
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(restaurant.getId(), result.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals("추가메뉴", result.getName());
        Assertions.assertEquals(10000L, result.getPrice());
    }

    @Test
    @DisplayName("메뉴 삭제")
    void test_removeMenu() {
        // given
        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_PARTNER);
        Restaurant restaurant = MockBuilder.mockRestaurant(user);
        Menu menu = MockBuilder.mockMenu(restaurant);
        restaurant.addMenu(menu);
        int originMenuSize = restaurant.getMenus().size();
        given(restaurantRepository.findById(anyLong()))
                .willReturn(Optional.of(restaurant));
        given(menuRepository.findByIdAndRestaurant(anyLong(), any()))
                .willReturn(Optional.of(menu));
        // when
        RestaurantDto result = restaurantService.removeMenu(1L, 1L);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(restaurant.getId(), result.getId());
        Assertions.assertEquals(restaurant.getName(), result.getName());
        Assertions.assertEquals(originMenuSize-1, result.getMenus().size());
    }
}
