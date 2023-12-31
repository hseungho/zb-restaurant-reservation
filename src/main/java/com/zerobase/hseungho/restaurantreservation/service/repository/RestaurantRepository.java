package com.zerobase.hseungho.restaurantreservation.service.repository;

import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.IRestaurantDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    boolean existsByManager(User user);

    @Query(value =
            "SELECT *, " +
            "ST_Distance_Sphere(POINT(:userX, :userY), POINT(r.x, r.y)) as distance " +
            "FROM restaurant r " +
            "WHERE (r.delete_req_at IS NULL AND r.deleted_at IS NULL) AND r.name LIKE %:name%",
    nativeQuery = true)
    Slice<IRestaurantDto> findByNameWithDistance(String name, Double userX, Double userY, Pageable pageable);

    @Query(value =
            "SELECT *, " +
            "ST_Distance_Sphere(POINT(:userX, :userY), POINT(r.x, r.y)) as distance " +
            "FROM restaurant r " +
            "WHERE (r.delete_req_at IS NULL AND r.deleted_at IS NULL) AND r.address LIKE %:address%",
            nativeQuery = true)
    Slice<IRestaurantDto> findByAddressWithDistance(String address, Double userX, Double userY, Pageable pageable);

    Optional<Restaurant> findByManager(User manager);

    List<Restaurant> findByDeleteReqAtBetween(LocalDateTime from, LocalDateTime to);

}
