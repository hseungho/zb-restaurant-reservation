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

    boolean isContainsRestaurantTimes(int hour, int minute) {
        return isContainsOpenTime(hour, minute) && isContainsCloseTime(hour, minute);
    }

    boolean isContainsOpenTime(int hour, int minute) {
        return this.openHour <= hour && this.openMinute <= minute;
    }

    boolean isContainsCloseTime(int hour, int minute) {
        return this.closeHour >= hour && this.closeMinute >= minute;
    }
}
