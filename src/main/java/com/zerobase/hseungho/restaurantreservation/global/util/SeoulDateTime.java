package com.zerobase.hseungho.restaurantreservation.global.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class SeoulDateTime {

    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

}
