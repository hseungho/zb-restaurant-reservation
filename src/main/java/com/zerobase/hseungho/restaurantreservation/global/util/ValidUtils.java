package com.zerobase.hseungho.restaurantreservation.global.util;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class ValidUtils {
    public static boolean isNonNull(Object... args) {
        for (Object arg : args) {
            if (ObjectUtils.isEmpty(arg)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasTexts(String... args) {
        for (String arg : args) {
            if (!StringUtils.hasText(arg)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMin(int minTarget, Integer... args) {
        for (Integer arg : args) {
            if (arg < minTarget) {
                return false;
            }
        }
        return true;
    }
}
