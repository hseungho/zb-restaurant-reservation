package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReservationDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReserveReservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;

public interface ReservationService {

    /**
     * 예약 요청 메소드. <br>
     * <br>
     * 예약 요청 매장과 요청 유저에 대한 예약을 저장한다. <br>
     * 매장을 DB에서 조회하고, 예약 요청에 대한 검증 로직을 거친 후, 예약 정보를 DB에 저장한다. <br>
     * <br>
     * 로직이 실패 시 일괄된 롤백을 진행하기 위해 <br>
     * 이에 대한 모든 로직을 하나의 트랜잭션을 여긴다. <br>
     * <br>
     * @param request 예약 요청 DTO 클래스
     * @return 예약 DTO 클래스
     */
    ReservationDto reserve(ReserveReservation.Request request);

    /**
     * 예약 취소 메소드. <br>
     * <br>
     * DB에 저장되어 있는 예약 정보를 예약 ID를 이용하여 조회하고, <br>
     * 예약 취소에 대한 검증 로직을 거친 후, 예약 정보를 취소 예약으로 변경한다. <br>
     * <br>
     * 로직이 실패 시 일괄된 롤백을 진행하기 위해 <br>
     * 이에 대한 모든 로직을 하나의 트랜잭션을 여긴다. <br>
     * <br>
     * @param reservationId 예약 취소할 예약 ID
     * @return 예약 DTO 클래스
     */
    ReservationDto cancel(Long reservationId);

    /**
     * 예약 승인 메소드. <br>
     * <br>
     * DB에 저장되어 있는 예약 정보를 예약 ID를 이용하여 조회하고, <br>
     * 예약 승인에 대한 검증 로직을 거친 후, 예약 정보를 승인 예약으로 변경한다. <br>
     * <br>
     * 로직이 실패 시 일괄된 롤백을 진행하기 위해 <br>
     * 이에 대한 모든 로직을 하나의 트랜잭션으로 여긴다. <br>
     * <br>
     * @param reservationId 예약 승인할 예약 ID
     * @return 예약 DTO 클래스
     */
    ReservationDto approve(Long reservationId);

    /**
     * 예약 거절 메소드. <br>
     * <br>
     * DB에 저장되어 있는 예약 정보를 예약 ID를 이용하여 조회하고, <br>
     * 예약 거절에 대한 검증 로직을 거친 후, 예약 정보를 거절 예약으로 변경한다. <br>
     * <br>
     * 로직이 실패 시 일괄된 롤백을 진행하기 위해 <br>
     * 이에 대한 모든 로직을 하나의 트랜잭션으로 여긴다. <br>
     * <br>
     * @param reservationId 예약 거절할 예약 ID
     * @return 예약 DTO 클래스
     */
    ReservationDto refuse(Long reservationId);

    /**
     * 예약 도착확인 메소드. <br>
     * <br>
     * DB에 저장되어 있는 예약 정보를 예약 ID를 이용하여 조회하고, <br>
     * 예약 도착확인에 대한 검증 로직을 거친 후, 예약 정보를 도착확인 예약으로 변경한다. <br>
     * <br>
     * 단, 예약자는 예약일시 10분 전부터 도착확인이 불가하고, 점장의 경우 언제나 가능하다. <br>
     * <br>
     * 로직 실패 시 일괄된 롤백을 진행하기 위해 <br>
     * 이에 대한 모든 로직을 하나의 트랜잭션으로 여긴다. <br>
     * <br>
     * @param reservationId 도착확인할 예약 ID
     * @return 예약 DTO 클래스
     */
    ReservationDto visit(Long reservationId);

    /**
     * 고객의 자신 예약 리스트 조회 메소드. <br>
     * <br>
     * 요청자의 예약 리스트 조회에 대한 검증 로직을 거친 후, 요청자의 예약 정보를 <br>
     * Slice 객체로 반환한다. 이 때, API 쿼리 파라미터로 date 가 존재할 경우, <br>
     * 해당 일자에 대한 예약 정보만을 반환한다.
     * <br>
     * @param date 요청일자
     * @param pageable 페이징 객체
     * @return 예약 DTO 클래스 리스트
     */
    Slice<ReservationDto> findClientReservations(LocalDate date, Pageable pageable);

    /**
     * 점장의 자신 매장 예약 리스트 조회 메소드. <br>
     * <br>
     * 요청자가 점장일 경우 자신 매장에 대한 예약 정보를 반환하는 메소드다. <br>
     * DB에서 요청자의 매장을 조회하고, 해당 매장을 기준으로 DB에서 예약 정보를 조회한다. <br>
     * 해당 로직에 대한 검증 로직을 거친 후, 자신 매장에 대한 예약 정보를 <br>
     * Slice 객체로 반환한다. 이 때, API 쿼리 파라미터로 date 가 존재할 경우, <br>
     * 해당 일자에 대한 예약 정보만을 반환한다. <br>
     * <br>
     * @param date 요청일자
     * @param pageable 페이징 객체
     * @return 예약 DTO 클래스 리스트
     */
    Slice<ReservationDto> findManagerReservations(LocalDate date, Pageable pageable);

    /**
     * 예약 상세정보 조회 메소드. <br>
     * <br>
     * DB에 저장되어 있는 예약 정보를 예약 ID를 기준으로 조회하여 반환한다. <br>
     * <br>
     * @param reservationId 조회할 예약 ID
     * @return 예약 DTO 클래스 리스트
     */
    ReservationDto findReservation(Long reservationId);
}
