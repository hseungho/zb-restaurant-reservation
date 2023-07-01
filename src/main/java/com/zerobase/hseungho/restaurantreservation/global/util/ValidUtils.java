package com.zerobase.hseungho.restaurantreservation.global.util;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class ValidUtils {

    public static boolean isSimilarBetweenText(String x, String y, Double criteriaSimilarity) {
        return similarityBetweenTexts(x, y) > criteriaSimilarity;
    }

    private static double similarityBetweenTexts(String x, String y) {
        double maxLength = Double.max(x.length(), y.length());
        if (maxLength > 0) {
            return (maxLength - org.apache.commons.lang3.StringUtils.getLevenshteinDistance(x, y)) / maxLength;
        }
        return 1.0;
    }

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

    public static boolean isMin(double minTarget, Double... args) {
        for (Double arg : args) {
            if (arg < minTarget) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMax(int maxTarget, Integer... args) {
        for (Integer arg : args) {
            if (arg > maxTarget) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMax(double maxTarget, Double... args) {
        for (Double arg : args) {
            if (arg > maxTarget) {
                return false;
            }
        }
        return true;
    }

    public static boolean isExactHour(Integer... args) {
        for (Integer arg : args) {
            if (arg < 0 || arg > 23) {
                return false;
            }
        }
        return true;
    }

    public static boolean isExactMinute(Integer... args) {
        for (Integer arg : args) {
            if (arg < 0 || arg > 59) {
                return false;
            }
        }
        return true;
    }

    public static boolean isTimeInMinutes(LocalDateTime time, int criteriaMinute) {
        return time.getMinute() % criteriaMinute == 0;
    }

    public static boolean isDifferenceFromNowLessThanMinutes(LocalDateTime time, int criteriaMinute) {
        LocalDateTime now = SeoulDateTime.now();
        return now.isBefore(time) ?
                now.plusMinutes(criteriaMinute).isAfter(time) :
                now.minusMinutes(criteriaMinute).isBefore(time);
    }

    public static boolean isAfterNow(LocalDateTime time) {
        return SeoulDateTime.now().isAfter(time);
    }
}
