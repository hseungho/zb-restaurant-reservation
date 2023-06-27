package com.zerobase.hseungho.restaurantreservation.service.repository;

import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.IRestaurantDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    boolean existsByManager(User user);

    @Query(value = "SELECT " +
            "r.id, r.name, r.address, " +
            "ST_Distance_Sphere(POINT(:userX, :userY), POINT(r.x, r.y)) as distance, " +
            "r.description, r.openHour, r.openMinute, " +
            "r.closeHour, r.closeMinute, r.countOfTables, " +
            "r.maxPerReservation, r.contactNumber, r.rating, " +
            "r.createdAt, r.updatedAt, r.deleteReqAt, r.deletedAt " +
            "FROM restaurant r " +
            "JOIN FETCH r.menus " +
            "JOIN FETCH r.reviews",
    nativeQuery = true)
    Slice<IRestaurantDto> findByNameCalculateDistance(String name, Double userX, Double userY, Pageable pageable);

}
