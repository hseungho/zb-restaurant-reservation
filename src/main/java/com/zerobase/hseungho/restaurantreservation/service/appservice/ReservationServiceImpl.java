package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.ForbiddenException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.InternalServerErrorException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.NotFoundException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.security.SecurityHolder;
import com.zerobase.hseungho.restaurantreservation.global.util.Generator;
import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import com.zerobase.hseungho.restaurantreservation.global.util.ValidUtils;
import com.zerobase.hseungho.restaurantreservation.service.domain.reservation.Reservation;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReservationDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReserveReservation;
import com.zerobase.hseungho.restaurantreservation.service.repository.ReservationRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public ReservationDto reserve(ReserveReservation.Request request) {
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));

        validateReserveRequest(user, restaurant, request);

        return ReservationDto.fromEntity(
                reservationRepository.save(
                        Reservation.create(
                                generateReservationNumber(),
                                request.getNumOfPerson(),
                                request.getClientContactNumber(),
                                request.getReservedAt(),
                                user,
                                restaurant
                        )
                )
        );
    }

    @Override
    @Transactional
    public ReservationDto cancel(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESERVATION));

        validateCancelRequest(reservation);

        reservation.cancel();

        return ReservationDto.fromEntity(reservation);
    }

    @Override
    @Transactional
    public ReservationDto approve(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESERVATION));

        validateApproveRequest(reservation);

        reservation.approve();

        return ReservationDto.fromEntity(reservation);
    }

    @Override
    @Transactional
    public ReservationDto refuse(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESERVATION));

        validateRefuseRequest(reservation);

        reservation.refuse();

        return ReservationDto.fromEntity(reservation);
    }

    @Override
    @Transactional
    public ReservationDto visit(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESERVATION));

        validateVisitRequest(reservation);

        reservation.visit();

        return ReservationDto.fromEntity(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<ReservationDto> findClientReservations(LocalDate date, Pageable pageable) {
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));
        if (user.isPartner()) {
            // 고객이 아닌 유저는 고객의 예약 리스트를 조회할 수 없습니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_FIND_RESERVATION_LIST_ONLY_CLIENT);
        }
        if (date == null) {
            return reservationRepository.findByClient(user, pageable)
                    .map(ReservationDto::fromEntity);
        } else {
            return reservationRepository.findByClientAndReservedAtBetween(user, date.atStartOfDay(), date.plusDays(1).atStartOfDay(), pageable)
                    .map(ReservationDto::fromEntity);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<ReservationDto> findManagerReservations(LocalDate date, Pageable pageable) {
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));
        if (!user.isPartner()) {
            // 파트너가 아닌 유저는 매장의 예약 리스트를 조회할 수 없습니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_FIND_RESERVATION_LIST_ONLY_MANAGER);
        }
        Restaurant restaurant = restaurantRepository.findByManager(user)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));
        if (date == null) {
            return reservationRepository.findByRestaurant(restaurant, pageable)
                    .map(ReservationDto::fromEntity);
        } else {
            return reservationRepository.findByRestaurantAndReservedAtBetween(restaurant, date.atStartOfDay(), date.plusDays(1).atStartOfDay(), pageable)
                    .map(ReservationDto::fromEntity);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationDto findReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESERVATION));

        validateFindRequest(reservation);

        return ReservationDto.fromEntity(reservation);
    }

    private void validateFindRequest(Reservation reservation) {
        if (reservation.isDeletedRestaurant()) {
            // 영업 종료된 매장의 예약은 조회할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_FIND_RESERVATION_DELETED_RESTAURANT);
        }
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));
        if (user.isPartner()) {
            if (!reservation.getRestaurant().isManager(user)) {
                // 예약 매장의 점장이 아닙니다.
                throw new ForbiddenException(ErrorCodeType.FORBIDDEN_FIND_RESERVATION_MANAGER_NOT_YOUR_RESOURCE);
            }
        } else {
            if (!reservation.isClient(user)) {
                // 예약의 예약자가 아닙니다.
                throw new ForbiddenException(ErrorCodeType.FORBIDDEN_FIND_RESERVATION_CLIENT_NOT_YOUR_RESOURCE);
            }
        }
    }

    private void validateVisitRequest(Reservation reservation) {
        if (reservation.isDeletedRestaurant()) {
            // 영업 종료된 매장의 예약을 도착확인할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_VISIT_RESERVATION_DELETED_RESTAURANT);
        }
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));
        if (user.isPartner()) {
            if (!reservation.getRestaurant().isManager(user)) {
                // 예약 매장의 점장이 아닙니다.
                throw new ForbiddenException(ErrorCodeType.FORBIDDEN_VISIT_RESERVATION_NOT_MANAGER_OF_RESTAURANT);
            }
        } else {
            if (!reservation.isClient(user)) {
                // 예약의 예약자가 아닙니다.
                throw new ForbiddenException(ErrorCodeType.FORBIDDEN_VISIT_RESERVATION_NOT_YOUR_RESOURCE);
            } else {
                if (ValidUtils.isDifferenceFromNowLessThanMinutes(reservation.getReservedAt(), 10)) {
                    // 예약시간의 10분 전부터는 도착확인할 수 없습니다.
                    throw new BadRequestException(ErrorCodeType.BAD_REQUEST_VISIT_RESERVATION_VISITED_TIME_CANNOT_LESS_THAN_TEN_MINUTES);
                }
                if (!ValidUtils.isDifferenceFromNowLessThanMinutes(reservation.getReservedAt(), 30)) {
                    // 도착확인은 예약시간 30분 전부터 도착확인할 수 있습니다.
                    throw new BadRequestException(ErrorCodeType.BAD_REQUEST_VISIT_RESERVATION_VISIT_CAN_THIRTY_MINUTES_BEFORE_RESERVED_AT);
                }
            }
        }
        if (reservation.isVisited()) {
            // 이미 도착확인된 예약입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_VISIT_RESERVATION_ALREADY_VISITED);
        }
        if (reservation.isReserved()) {
            // 도착확인하기 위해선 점장이 예약을 승인해야합니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_VISIT_RESERVATION_RESERVED_STATUS_CANNOT_VISIT);
        }
        if (!reservation.isApproved()) {
            // 도착확인할 수 없는 예약 상태입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_VISIT_RESERVATION_STATUS_IS_NOT_SUITED_VISIT);
        }
    }

    private void validateRefuseRequest(Reservation reservation) {
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));
        if (!user.isPartner()) {
            // 파트너가 아닌 유저는 예약을 거절할 수 없습니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_REFUSE_RESERVATION_CUSTOMER_CANNOT_REFUSE);
        }
        if (reservation.isDeletedRestaurant()) {
            // 영업 종료된 매장의 예약을 거절할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_REFUSE_RESERVATION_DELETED_RESTAURANT);
        }
        if (!reservation.getRestaurant().isManager(user)) {
            // 해당 매장의 점장이 아닌 유저는 예약을 승인할 수 없습니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_REFUSE_RESERVATION_NOT_MANAGER_OF_RESTAURANT);
        }
        if (reservation.isCanceled()) {
            // 이미 취소된 예약입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_REFUSE_RESERVATION_ALREADY_CANCELED);
        }
        if (reservation.isRefused()) {
            // 이미 거절된 예약입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_REFUSE_RESERVATION_ALREADY_REFUSED);
        }
        if (!reservation.isReserved()) {
            // 거절할 수 없는 예약 상태입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_REFUSE_RESERVATION_STATUS_IS_NOT_SUITED_REFUSE);
        }
    }

    private void validateApproveRequest(Reservation reservation) {
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));
        if (!user.isPartner()) {
            // 파트너가 아닌 유저는 예약을 승인할 수 없습니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_APPROVE_RESERVATION_CUSTOMER_CANNOT_APPROVE);
        }
        if (reservation.isDeletedRestaurant()) {
            // 영업 종료된 매장의 예약을 승인할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_APPROVE_RESERVATION_DELETED_RESTAURANT);
        }
        if (!reservation.getRestaurant().isManager(user)) {
            // 해당 매장의 점장이 아닌 유저는 예약을 승인할 수 없습니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_APPROVE_RESERVATION_NOT_MANAGER_OF_RESTAURANT);
        }
        if (reservation.isCanceled()) {
            // 이미 취소된 예약입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_APPROVE_RESERVATION_ALREADY_CANCELED);
        }
        if (reservation.isApproved()) {
            // 이미 승인된 예약입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_APPROVE_RESERVATION_ALREADY_APPROVED);
        }
        if (!reservation.isReserved()) {
            // 승인할 수 없는 예약 상태입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_APPROVE_RESERVATION_STATUS_IS_NOT_SUITED_APPROVE);
        }
    }

    private void validateCancelRequest(Reservation reservation) {
        if (!isReservationClient(reservation)) {
            // 다른 고객의 예약을 취소할 수 없습니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_CANCEL_RESERVATION_NOT_YOUR_RESOURCE);
        }
        if (reservation.isDeletedRestaurant()) {
            // 영업 종료된 매장의 예약은 취소할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_CANCEL_RESERVATION_DELETED_RESTAURANT);
        }
        if (ValidUtils.isDifferenceFromNowLessThanMinutes(reservation.getReservedAt(), 30)) {
            // 예약 30분 전에는 취소할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_CANCEL_RESERVATION_CANCELED_TIME_CANNOT_LESS_THAN_THIRTY_MINUTES);
        }
        if (reservation.isCanceled()) {
            // 이미 취소된 예약입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_CANCEL_RESERVATION_ALREADY_CANCELED);
        }
    }

    private boolean isReservationClient(Reservation reservation) {
        return reservation.isClient(userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER)));
    }

    private String generateReservationNumber() {
        AtomicInteger i = new AtomicInteger(10);
        while (i.getAndDecrement() > 0) {
            String number = Generator.generateReservationNumber();
            if(!reservationRepository.existsByNumber(number)) {
                return number;
            }
        }
        throw new InternalServerErrorException(ErrorCodeType.INTERNAL_SERVER_ERROR_GENERATE_RESERVATION_NUMBER);
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
        if (request.getReservedAt().isBefore(SeoulDateTime.now())) {
            // 현재시간보다 이전 시간을 예약할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESERVE_RESERVATION_RESERVED_TIME_IS_BEFORE_NOW);
        }
        if (!restaurant.isValidReserveAt(request.getReservedAt())) {
            // 예약일시는 매장 오픈 및 마감 시간에 맞게 요청해주세요.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESERVE_RESERVATION_RESERVED_TIME_IS_INVALID_RESTAURANT_TIME);
        }
        if (!ValidUtils.isTimeInMinutes(request.getReservedAt(), 5)) {
            // 예약일시는 5분 단위로만 요청가능합니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESERVE_RESERVATION_RESERVED_TIME_IS_NOT_IN_FIVE_MINUTES);
        }
        if (ValidUtils.isDifferenceFromNowLessThanMinutes(request.getReservedAt(), 10)) {
            // 현재시간의 10분 후 시간은 예약하실 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESERVE_RESERVATION_RESERVED_TIME_CANNOT_LESS_THAN_TEN_MINUTES);
        }
        if (restaurant.getMaxPerReservation() < request.getNumOfPerson()) {
            // 예약 인원이 매장에서 설정한 최대 인원을 벗어납니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESERVE_RESERVATION_NUM_OF_PERSON_OVER_MAX_PER_RESERVATION);
        }
        if (this.isEqualsTablesBetweenReservations(restaurant, request.getReservedAt())) {
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
