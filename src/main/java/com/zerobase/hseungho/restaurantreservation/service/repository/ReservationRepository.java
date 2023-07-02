package com.zerobase.hseungho.restaurantreservation.service.repository;

import com.zerobase.hseungho.restaurantreservation.service.domain.reservation.Reservation;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.type.ReservationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    long countByRestaurantAndReservedAt(Restaurant restaurant, LocalDateTime reservedAt);

    boolean existsByNumber(String number);

    @Query(
            value = "SELECT rv " +
                    "FROM reservation rv " +
                    "WHERE rv.client = :client AND rv.restaurant IN " +
                    "(SELECT rt FROM restaurant rt WHERE rt.deleteReqAt IS NULL AND rt.deletedAt IS NULL) "
    )
    Slice<Reservation> findByClientAndDeletedRestaurantNot(User client, Pageable pageable);

    @Query(
            value = "SELECT rv " +
                    "FROM reservation rv " +
                    "WHERE rv.client = :client AND (rv.reservedAt BETWEEN :from AND :to) AND rv.restaurant IN " +
                    "(SELECT rt FROM restaurant rt WHERE rt.deleteReqAt IS NULL AND rt.deletedAt IS NULL)"
    )
    Slice<Reservation> findByClientAndDeletedRestaurantNotAndReservedAtBetween(User client, LocalDateTime from, LocalDateTime to, Pageable pageable);

    @Query(
            value = "SELECT rv " +
                    "FROM reservation rv " +
                    "WHERE rv.restaurant = :restaurant AND rv.restaurant IN " +
                    "(SELECT rt FROM restaurant rt WHERE rt.deleteReqAt IS NULL AND rt.deletedAt IS NULL)"
    )
    Slice<Reservation> findByRestaurantAndDeletedRestaurantNot(Restaurant restaurant, Pageable pageable);

    @Query(
            value = "SELECT rv " +
                    "FROM reservation rv " +
                    "WHERE rv.restaurant = :restaurant AND (rv.reservedAt BETWEEN :from AND :to) AND rv.restaurant IN " +
                    "(SELECT rt FROM restaurant rt WHERE rt.deleteReqAt IS NULL AND rt.deletedAt IS NULL)"
    )
    Slice<Reservation> findByRestaurantAndDeletedRestaurantNotAndReservedAtBetween(Restaurant restaurant, LocalDateTime from, LocalDateTime to, Pageable pageable);

    boolean existsByRestaurantAndStatusAndReservedAtGreaterThanEqual(Restaurant restaurant, ReservationStatus status, LocalDateTime requestedTime);
}
