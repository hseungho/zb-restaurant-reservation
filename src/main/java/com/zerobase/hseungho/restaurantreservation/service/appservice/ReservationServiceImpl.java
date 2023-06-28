package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.NotFoundException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.security.SecurityHolder;
import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import com.zerobase.hseungho.restaurantreservation.global.util.ValidUtils;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReservationDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReserveReservation;
import com.zerobase.hseungho.restaurantreservation.service.repository.ReservationRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public ReservationDto reserve(Long restaurantId, ReserveReservation.Request request) {
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));

        validateReserveRequest(user, restaurant, request);


        return null;
    }

    private void validateReserveRequest(User user,
                                        Restaurant restaurant,
                                        ReserveReservation.Request request) {
        if (!ValidUtils.isNonNull(request.getReservedAt())
                || !ValidUtils.isMin(1, request.getNumOfPerson())
                || !ValidUtils.hasTexts(request.getClientContactNumber())) {
            // 모든 파라미터를 올바르게 입력해주세요.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESERVE_RESERVATION_BLANK);
        }
        if (restaurant.isManager(user)) {
            // 점장은 예약 요청할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESERVE_RESERVATION_RESERVING_CANNOT_MANAGER);
        }
        if (!restaurant.isValidReserveAt(request.getReservedAt())) {
            // 예약일시는 매장 오픈 및 마감 시간에 맞게 요청해주세요.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESERVE_RESERVATION_RESERVED_TIME_IS_INVALID_RESTAURANT_TIME);
        }
        if (!ValidUtils.isTimeInMinutes(request.getReservedAt(), 5)) {
            // 예약일시는 5분 단위로만 요청가능합니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESERVE_RESERVATION_RESERVED_TIME_IS_NOT_IN_FIVE_MINUTES);
        }
        if (SeoulDateTime.isDifferenceFromNowLessThanMinutes(request.getReservedAt(), 10)) {
            // 현재시간의 10분 후 시간은 예약하실 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESERVE_RESERVATION_RESERVED_TIME_CANNOT_LESS_THAN_TEN_MINUTES);
        }
        if (restaurant.getMaxPerReservation() < request.getNumOfPerson()) {
            // 예약 인원이 매장에서 설정한 최대 인원을 벗어납니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESERVE_RESERVATION_NUM_OF_PERSON_OVER_MAX_PER_RESERVATION);
        }
        if (isEqualsTablesBetweenReservations(restaurant, request.getReservedAt())) {
            // 해당 예약일시의 예약 수가 만석입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESERVE_RESERVATION_RESERVED_TIME_IS_FULL);
        }
        if (restaurant.isAfterDeleteReqAt(request.getReservedAt())) {
            // 해당 예약일시는 매장 사정으로 인해 불가능합니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESERVE_RESERVATION_REQ_DATE_AFTER_DELETED_RESTAURANT);
        }
    }

    private boolean isEqualsTablesBetweenReservations(Restaurant restaurant, LocalDateTime reservedAt) {
        Integer countOfTables = restaurant.getCountOfTables();
        long countOfReserved = reservationRepository.countByRestaurantAndReservedAt(restaurant, reservedAt);
        return countOfReserved >= countOfTables;
    }
}
