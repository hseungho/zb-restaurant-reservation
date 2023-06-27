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

    @Query(value =
            "SELECT *, " +
            "ST_Distance_Sphere(POINT(:userX, :userY), POINT(r.x, r.y)) as distance " +
            "FROM restaurant r " +
            "WHERE r.name LIKE :name%",
    nativeQuery = true)
    Slice<IRestaurantDto> findByNameCalculateDistance(String name, Double userX, Double userY, Pageable pageable);

}
