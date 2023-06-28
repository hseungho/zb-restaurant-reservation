package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Embeddable
@NoArgsConstructor
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

    @Transient
    private LocalTime open;
    @Transient
    private LocalTime close;

    public RestaurantTimeVO(Integer openHour, Integer openMinute, Integer closeHour, Integer closeMinute) {
        this.openHour = openHour;
        this.openMinute = openMinute;
        this.closeHour = closeHour;
        this.closeMinute = closeMinute;
        this.open = LocalTime.of(this.openHour, this.openMinute);
        this.close = LocalTime.of(this.closeHour, this.closeMinute);
    }

    boolean isContainsRestaurantTimes(int hour, int minute) {
        return isContainsOpenTime(hour, minute) && isContainsCloseTime(hour, minute);
    }

    boolean isContainsOpenTime(int hour, int minute) {
        LocalTime req = LocalTime.of(hour, minute);
        return this.open.isBefore(req);
    }

    boolean isContainsCloseTime(int hour, int minute) {
        LocalTime req = LocalTime.of(hour, minute);
        return this.close.isAfter(req);
    }
}
