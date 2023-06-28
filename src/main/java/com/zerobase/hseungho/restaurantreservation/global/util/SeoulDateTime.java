package com.zerobase.hseungho.restaurantreservation.global.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class SeoulDateTime {

    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public static boolean isDifferenceFromNowLessThanMinutes(LocalDateTime time, int criteriaMinute) {
        LocalDateTime now = SeoulDateTime.now();
        return now.isBefore(time) ?
                now.plusMinutes(criteriaMinute).isAfter(time) :
                now.minusMinutes(criteriaMinute).isBefore(time);
    }

}
