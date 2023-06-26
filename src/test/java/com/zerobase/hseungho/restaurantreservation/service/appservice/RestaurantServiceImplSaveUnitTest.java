package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import com.zerobase.hseungho.restaurantreservation.util.TestSecurityHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        // when
        // then
    }

}
