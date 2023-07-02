package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.adapter.webclient.KakaoWebClientComponent;
import com.zerobase.hseungho.restaurantreservation.global.adapter.webclient.dto.CoordinateDto;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.UpdateRestaurant;
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

}
