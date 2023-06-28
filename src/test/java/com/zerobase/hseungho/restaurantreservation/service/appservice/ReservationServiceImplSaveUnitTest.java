package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import com.zerobase.hseungho.restaurantreservation.service.domain.reservation.Reservation;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReservationDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReserveReservation;
import com.zerobase.hseungho.restaurantreservation.service.repository.ReservationRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import com.zerobase.hseungho.restaurantreservation.service.type.ReservationStatus;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import com.zerobase.hseungho.restaurantreservation.util.MockBuilder;
import com.zerobase.hseungho.restaurantreservation.util.TestSecurityHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplSaveUnitTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("예약 요청 성공")
    void test_reserve_success() {
        // given
        LocalDateTime now = SeoulDateTime.now();
        LocalDateTime reservedAt = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 1, MockBuilder.MOCK_OPEN_HOUR + 2, MockBuilder.MOCK_OPEN_MINUTE + 10);

        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_CUSTOMER);
        Restaurant restaurant = MockBuilder.mockRestaurant(MockBuilder.mockUser(UserType.ROLE_PARTNER));
        given(userRepository.findById(anyString()))
                .willReturn(Optional.of(user));
        given(restaurantRepository.findById(anyLong()))
                .willReturn(Optional.of(restaurant));
        given(reservationRepository.countByRestaurantAndReservedAt(any(), any()))
                .willReturn(0L);
        given(reservationRepository.existsByNumber(anyString()))
                .willReturn(false);
        given(reservationRepository.save(any()))
                .willReturn(MockBuilder.mockReservation(
                        reservedAt, ReservationStatus.RESERVED, user, restaurant));
        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        // when
        ReservationDto result = reservationService.reserve(request(reservedAt));
        // then
        verify(reservationRepository, times(1)).save(captor.capture());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(MockBuilder.MOCK_RESERVATION_ID, result.getId());
        Assertions.assertEquals(MockBuilder.MOCK_RESERVATION_NUMBER, result.getNumber());
        Assertions.assertEquals(MockBuilder.MOCK_NUM_OF_PERSON, result.getNumOfPerson());
        Assertions.assertEquals(MockBuilder.MOCK_CLIENT_CONTACT_NUMBER, result.getClientContactNumber());
        Assertions.assertEquals(reservedAt.toString(), result.getReservedAt().toString());
        Assertions.assertEquals(ReservationStatus.RESERVED, result.getStatus());
        Assertions.assertEquals(user.getId(), result.getClient().getId());
        Assertions.assertEquals(restaurant.getId(), result.getRestaurant().getId());
    }

    private ReserveReservation.Request request(LocalDateTime reservedAt) {
        return ReserveReservation.Request.builder()
                .restaurantId(1L)
                .reservedAt(reservedAt)
                .numOfPerson(MockBuilder.MOCK_NUM_OF_PERSON)
                .clientContactNumber(MockBuilder.MOCK_CLIENT_CONTACT_NUMBER)
                .build();
    }
}
