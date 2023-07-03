package com.zerobase.hseungho.restaurantreservation.global.batch;

import com.zerobase.hseungho.restaurantreservation.global.adapter.fileupload.AwsS3ImageManager;
import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDate;
import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import com.zerobase.hseungho.restaurantreservation.service.domain.reservation.Reservation;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Review;
import com.zerobase.hseungho.restaurantreservation.service.repository.ReservationRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerComponent {

    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;
    private final AwsS3ImageManager imageManager;

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
        log.info("SCHEDULER -> reservations status manage success. size -> {}", reservations.size());
    }

    /**
     * 매일 오전 2시에 해당 일자에 삭제 요청한 매장들을 삭제 진행하는 스케쥴러 메소드.
     */
    @Async
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Scheduled(cron ="0 2 * * * *", zone = "Asia/Seoul")
    public void deleteRestaurantByTheirDeleteReqAt() {
        LocalDate today = SeoulDate.now();
        List<Restaurant> restaurants = restaurantRepository.findByDeleteReqAtBetween(today.atStartOfDay(), today.atTime(23, 59, 59));
        log.info("SCHEDULER - restaurants of deleteReqAt today's size -> {}", restaurants.size());

        for (Restaurant restaurant : restaurants) {
            List<Review> reviews = restaurant.getReviews();
            if (!CollectionUtils.isEmpty(reviews)) {
                reviews.stream()
                        .filter(it -> it.getImageSrc() != null)
                        .peek(it -> {
                            imageManager.delete(it.getImageSrc());
                            try {
                                Thread.sleep(200L);
                            } catch (InterruptedException e) {
                                log.error("SCHEDULER - occurred InterruptedException during delete reviews' image -> id: {}", it.getId());
                            }
                        })
                        .close();
            }

            restaurant.delete(SeoulDateTime.now());
        }
        log.info("SCHEDULER -> delete restaurant by their delete request time success. size -> {}", restaurants.size());
    }
}
