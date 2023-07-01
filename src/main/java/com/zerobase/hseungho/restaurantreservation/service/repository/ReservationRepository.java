package com.zerobase.hseungho.restaurantreservation.service.repository;

import com.zerobase.hseungho.restaurantreservation.service.domain.reservation.Reservation;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    long countByRestaurantAndReservedAt(Restaurant restaurant, LocalDateTime reservedAt);

    boolean existsByNumber(String number);

    Slice<Reservation> findByClient(User client, Pageable pageable);

    Slice<Reservation> findByClientAndReservedAtBetween(User client, LocalDateTime from, LocalDateTime to, Pageable pageable);

    Slice<Reservation> findByRestaurant(Restaurant restaurant, Pageable pageable);

    Slice<Reservation> findByRestaurantAndReservedAtBetween(Restaurant restaurant, LocalDateTime from, LocalDateTime to, Pageable pageable);

    boolean existsByRestaurantAndReservedAtGreaterThanEqual(Restaurant restaurant, LocalDateTime requestedTime);
}
