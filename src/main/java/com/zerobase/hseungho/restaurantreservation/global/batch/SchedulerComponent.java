package com.zerobase.hseungho.restaurantreservation.global.batch;

import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import com.zerobase.hseungho.restaurantreservation.service.domain.reservation.Reservation;
import com.zerobase.hseungho.restaurantreservation.service.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerComponent {

    private final ReservationRepository reservationRepository;

    /**
     * 5분 간격으로 DB 상의 예약 정보를 가져와서 해당 시간 - 5분 이전에 대한  <br>
     * 예약에 대해서 예약 상태를 검증하고 현황과 다를 경우, 상태를 변경한다. <br>
     * <br>
     * - 예약 시간까지 예약 승인이 되지 않은 예약은 자동으로 거절로 변경. <br>
     * - 예약 시간까지 예약 승인이 되었으나 도착확인이 되지 않은 예약은 자동으로 취소로 변경.
     */
    @Async
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Scheduled(cron = "0 */5 * * * *", zone = "Asia/Seoul")
    public void checkReservationStatusCurrentSituationInFiveMinutes() {
        LocalDateTime time = SeoulDateTime.now().minusMinutes(5);

        List<Reservation> reservations = reservationRepository.findByStatusIsReservedOrApprovedAndReservedAtBefore(time);
        log.info("SCHEDULER - reservations of reservedAt before now's size -> {}", reservations.size());

        for (Reservation reservation : reservations) {
            if (reservation.isReserved()) {
                reservation.refuse();
                log.info("SCHEDULER - id {} is reserved now. this changed to refuse.", reservation.getId());
            } else if (reservation.isApproved()) {
                reservation.cancel();
                log.info("SCHEDULER - id {} is approved now. this changed to cancel.", reservation.getId());
            }
        }
    }

}
