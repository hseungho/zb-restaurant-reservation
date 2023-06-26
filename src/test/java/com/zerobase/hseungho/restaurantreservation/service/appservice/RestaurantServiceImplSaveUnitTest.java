package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.SaveRestaurant;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import com.zerobase.hseungho.restaurantreservation.util.TestSecurityHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplSaveUnitTest {

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("매장 등록 성공")
    void test_saveRestaurant_success() {
        // given
        TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_PARTNER);
        given(restaurantRepository.existsByManager(any()))
                .willReturn(false);
        ArgumentCaptor<Restaurant> captor = ArgumentCaptor.forClass(Restaurant.class);
        // when
//        restaurantService.save(
//                SaveRestaurant.Request.builder()
//        )
        // then
    }

    private SaveRestaurant.Request forTest() {
        return SaveRestaurant.Request.builder()
                .name("매장이름")
                .address("서울 서대문구 증가로 12")
                .description("매장 설명입니다.")
                .build();
    }

}
