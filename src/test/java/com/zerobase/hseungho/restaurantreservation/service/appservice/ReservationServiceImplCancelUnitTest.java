package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import com.zerobase.hseungho.restaurantreservation.service.domain.reservation.Reservation;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReservationDto;
import com.zerobase.hseungho.restaurantreservation.service.repository.ReservationRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import com.zerobase.hseungho.restaurantreservation.service.type.ReservationStatus;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplCancelUnitTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("예약 취소 성공")
    void test_cancel_success() {
        // given
        LocalDateTime now = SeoulDateTime.now();
        LocalDateTime reservedAt = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 1, MockBuilder.MOCK_OPEN_HOUR + 2, MockBuilder.MOCK_OPEN_MINUTE + 10);

        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_CUSTOMER);
        Restaurant restaurant = MockBuilder.mockRestaurant(MockBuilder.mockUser(UserType.ROLE_PARTNER));
        Reservation reservation = MockBuilder.mockReservation(reservedAt, ReservationStatus.RESERVED, user, restaurant);

        given(reservationRepository.findById(anyLong()))
                .willReturn(Optional.of(reservation));
        given(userRepository.findById(anyString()))
                .willReturn(Optional.of(user));
        // when
        ReservationDto result = reservationService.cancel(1L);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(MockBuilder.MOCK_RESERVATION_ID, result.getId());
        Assertions.assertEquals(MockBuilder.MOCK_RESERVATION_NUMBER, result.getNumber());
        Assertions.assertEquals(MockBuilder.MOCK_CLIENT_CONTACT_NUMBER, result.getClientContactNumber());
        Assertions.assertEquals(ReservationStatus.CANCELED, result.getStatus());
        Assertions.assertNotNull(result.getCreatedAt());
        Assertions.assertNotNull(result.getReservedAt());
        Assertions.assertNotNull(result.getCanceledAt());
        Assertions.assertEquals(user.getId(), result.getClient().getId());
        Assertions.assertEquals(restaurant.getId(), result.getRestaurant().getId());
    }

}
