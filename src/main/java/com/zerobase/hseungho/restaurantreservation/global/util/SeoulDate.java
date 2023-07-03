package com.zerobase.hseungho.restaurantreservation.global.util;

import java.time.LocalDate;
import java.time.ZoneId;

public class SeoulDate {

    /**
     * LocalDate의 Zone을 Asia/Seoul로 지정하여 현재 날짜를 반환하는 메소드.
     * @return Asia/Seoul로 지정된 현재 날짜 LocalDate 객체
     */
    public static LocalDate now() {
        return LocalDate.now(ZoneId.of("Asia/Seoul"));
    }
}
