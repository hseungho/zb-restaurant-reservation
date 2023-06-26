package com.zerobase.hseungho.restaurantreservation.global.util;

import org.springframework.util.StringUtils;

public class ValidUtils {
    public static boolean hasTexts(String... args) {
        for (String arg : args) {
            if (!StringUtils.hasText(arg)) {
                return false;
            }
        }
        return true;
    }
}
