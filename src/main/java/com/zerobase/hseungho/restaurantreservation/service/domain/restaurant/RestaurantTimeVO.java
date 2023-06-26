package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RestaurantTimeVO {
    private Integer openHour;
    private Integer openMinute;
    private Integer closeHour;
    private Integer closeMinute;
}
