package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReservationDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReserveReservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;

public interface ReservationService {

    /**
     * 예약 요청 메소드.
     * @param restaurantId 예약 요청할 매장 ID
     * @param request 예약 요청 DTO 클래스
     * @return 예약 DTO 클래스
     */
    ReservationDto reserve(ReserveReservation.Request request);

    /**
     * 예약 취소 메소드.
     * @param reservationId 예약 취소할 예약 ID
     * @return 예약 DTO 클래스
     */
    ReservationDto cancel(Long reservationId);

    /**
     * 예약 승인 메소드.
     * @param reservationId 예약 승인할 예약 ID
     * @return 예약 DTO 클래스
     */
    ReservationDto approve(Long reservationId);

    /**
     * 예약 거절 메소드.
     * @param reservationId 예약 거절할 예약 ID
     * @return 예약 DTO 클래스
     */
    ReservationDto refuse(Long reservationId);

    /**
     * 예약 도착확인 메소드. <br>
     * 예약자는 예약일시 10분전부터 불가, 하지만 점장은 언제나 가능.
     * @param reservationId 도착확인할 예약 ID
     * @return 예약 DTO 클래스
     */
    ReservationDto visit(Long reservationId);

    /**
     * 고객의 자신 예약 리스트 조회 메소드.
     * @param date 요청일자
     * @param pageable 페이징 객체
     * @return 예약 DTO 클래스 리스트
     */
    Slice<ReservationDto> findClientReservations(LocalDate date, Pageable pageable);
}
