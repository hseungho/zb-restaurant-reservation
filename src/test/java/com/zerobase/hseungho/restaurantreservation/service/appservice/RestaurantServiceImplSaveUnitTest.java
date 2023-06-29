package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.adapter.webclient.KakaoWebClientComponent;
import com.zerobase.hseungho.restaurantreservation.global.adapter.webclient.dto.CoordinateDto;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SaveRestaurant;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import com.zerobase.hseungho.restaurantreservation.util.MockBuilder;
import com.zerobase.hseungho.restaurantreservation.util.TestSecurityHolder;
import org.apache.commons.collections4.Trie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplSaveUnitTest {

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private KakaoWebClientComponent kakaoWebClientComponent;
    @Mock
    private Trie<String, String> trie;

    private final String restName = "매장이름";
    private final String restAddr = "서울 서대문구 증가로 12";
    private final String restDesc = "매장 설명입니다.";
    private final String menuName1 = "메뉴1";
    private final Long menuPrice1 = 10000L;
    private final String menuName2 = "메뉴2";
    private final Long menuPrice2 = 20000L;
    private final int openHour = 10;
    private final int openMinute = 0;
    private final int closeHour = 22;
    private final int closeMinute = 0;
    private final int countOfTables = 10;
    private final int maxPerReservation = 4;
    private final String contactNumber = "021231234";

    @Test
    @DisplayName("매장 등록 성공")
    void test_saveRestaurant_success() {
        // given
        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_PARTNER);
        given(userRepository.findById(anyString()))
                .willReturn(Optional.of(user));
        given(restaurantRepository.existsByManager(any()))
                .willReturn(false);
        given(kakaoWebClientComponent.getCoordinateByAddress(anyString()))
                .willReturn(new CoordinateDto(123.123, 321.321));
        given(restaurantRepository.save(any()))
                .willReturn(MockBuilder.mockRestaurant(user));
        given(trie.put(anyString(), anyString()))
                .willReturn(null);
        ArgumentCaptor<Restaurant> captor = ArgumentCaptor.forClass(Restaurant.class);
        // when
        RestaurantDto restaurantDto = restaurantService.saveRestaurant(forRequestTest());
        // then
        verify(restaurantRepository, times(1)).save(captor.capture());
        Assertions.assertNotNull(restaurantDto);
        Assertions.assertEquals("매장이름", restaurantDto.getName());
        Assertions.assertEquals(restName, restaurantDto.getName());
        Assertions.assertEquals(restAddr, restaurantDto.getAddress());
        Assertions.assertEquals(restDesc, restaurantDto.getDescription());
        Assertions.assertEquals(2, restaurantDto.getMenus().size());
        restaurantDto.getMenus().forEach(e -> {
            Assertions.assertTrue((e.getName().equals(menuName1) || e.getName().equals(menuName2)));
            Assertions.assertTrue((Objects.equals(e.getPrice(), menuPrice1) || Objects.equals(e.getPrice(), menuPrice2)));
        });
        Assertions.assertEquals(openHour, restaurantDto.getOpenTime().getHour());
        Assertions.assertEquals(openMinute, restaurantDto.getOpenTime().getMinute());
        Assertions.assertEquals(closeHour, restaurantDto.getCloseTime().getHour());
        Assertions.assertEquals(closeMinute, restaurantDto.getCloseTime().getMinute());
        Assertions.assertEquals(countOfTables, restaurantDto.getCountOfTables());
        Assertions.assertEquals(maxPerReservation, restaurantDto.getMaxPerReservation());
        Assertions.assertEquals(contactNumber, restaurantDto.getContactNumber());
        Assertions.assertEquals(user.getUserId(), restaurantDto.getManager().getUserId());
    }

    private SaveRestaurant.Request forRequestTest() {
        return SaveRestaurant.Request.builder()
                .name(restName)
                .address(restAddr)
                .description(restDesc)
                .menus(List.of(
                        SaveRestaurant.SaveMenu.Request.builder()
                                .name(menuName1).price(menuPrice1).build(),
                        SaveRestaurant.SaveMenu.Request.builder()
                                .name(menuName2).price(menuPrice2).build()
                        ))
                .openTime(SaveRestaurant.SaveRestaurantTime.Request.builder()
                        .hour(openHour).minute(openMinute).build())
                .closeTime(SaveRestaurant.SaveRestaurantTime.Request.builder()
                        .hour(closeHour).minute(closeMinute).build())
                .countOfTables(countOfTables)
                .maxPerReservation(maxPerReservation)
                .contactNumber(contactNumber)
                .build();
    }

}
