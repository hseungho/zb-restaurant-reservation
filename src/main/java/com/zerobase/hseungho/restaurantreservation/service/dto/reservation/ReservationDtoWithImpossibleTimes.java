package com.zerobase.hseungho.restaurantreservation.service.dto.reservation;

import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDtoWithImpossibleTimes {

    private RestaurantDto restaurant;
    private List<LocalDateTime> impossibleReservationTimes;

}
