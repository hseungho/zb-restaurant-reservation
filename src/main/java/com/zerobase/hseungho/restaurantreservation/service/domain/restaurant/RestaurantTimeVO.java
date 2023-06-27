package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RestaurantTimeVO {
    @Column(name = "open_hour", nullable = false)
    private Integer openHour;
    @Column(name = "open_minute", nullable = false)
    private Integer openMinute;
    @Column(name = "close_hour", nullable = false)
    private Integer closeHour;
    @Column(name = "close_minute", nullable = false)
    private Integer closeMinute;
}
