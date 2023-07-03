package com.zerobase.hseungho.restaurantreservation.global.util;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class ValidUtils {

    /**
     * 두 문자열의 유사도를 확인하는 메소드. <br>
     * 두 문자열의 유사도가 조건 유사도보다 이상이면
     * @param x y와 유사도를 확인할 문자열
     * @param y x와 유사도를 확인할 문자열
     * @param criteriaSimilarity 조건 유사도
     * @return 두 문자열의 유사도가 조건 유사도보다 이상이면 true.
     */
    public static boolean isSimilarBetweenText(String x, String y, Double criteriaSimilarity) {
        return similarityBetweenTexts(x, y) > criteriaSimilarity;
    }

    /**
     * apache 의 오픈 라이센스 중 StringUtils 라이브러리의 getLevenshteinDistance 메소드를 활용하여 <br>
     * 두 문자열의 유사도를 반환하는 메소드.
     * @param x y와 유사도를 확인할 문자열
     * @param y x와 유사도를 확인할 문자열
     * @return 두 문자열의 유사도
     */
    private static double similarityBetweenTexts(String x, String y) {
        double maxLength = Double.max(x.length(), y.length());
        if (maxLength > 0) {
            return (maxLength - org.apache.commons.lang3.StringUtils.getLevenshteinDistance(x, y)) / maxLength;
        }
        return 1.0;
    }

    /**
     * 파라미터가 NonNull 인지 검증하는 메소드.
     * @param args 검증할 파라미터들
     * @return NonNull 이면 true
     */
    public static boolean isNonNull(Object... args) {
        for (Object arg : args) {
            if (ObjectUtils.isEmpty(arg)) {
                return false;
            }
        }
        return true;
    }

    /**
     * String 파라미터가 value 를 가지고 있는지 검증하는 메소드.
     * @param args 검증할 String 파라미터들
     * @return String 값이 value 를 가지고 있으면 true
     */
    public static boolean hasTexts(String... args) {
        for (String arg : args) {
            if (!StringUtils.hasText(arg)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Int 값이 특정 값보다 작은 값인지를 검증하는 메소드.
     * @param minTarget 검증 조건 최소값
     * @param args 검증할 Int 파라미터들
     * @return Int 값이 최소값보다 이하면 true
     */
    public static boolean isLessThan(int minTarget, int... args) {
        for (int arg : args) {
            if (arg >= minTarget) {
                return false;
            }
        }
        return true;
    }

    /**
     * Long 값이 특정 값보다 작은 값인지를 검증하는 메소드.
     * @param minTarget 검증 조건 최소값
     * @param args 검증할 Long 파라미터들
     * @return Long 값이 최소값보다 이하면 true
     */
    public static boolean isLessThan(long minTarget, long... args) {
        for (long arg : args) {
            if (arg >= minTarget) {
                return false;
            }
        }
        return true;
    }

    /**
     * Double 값이 특정 값보다 작은 값인지를 검증하는 메소드.
     * @param minTarget 검증 조건 최소값
     * @param args 검증할 Double 파라미터들
     * @return Double 값이 최소값보다 이하면 true
     */
    public static boolean isLessThan(double minTarget, double... args) {
        for (double arg : args) {
            if (arg >= minTarget) {
                return false;
            }
        }
        return true;
    }

    /**
     * Int 값이 특정 값보다 큰 값인지를 검증하는 메소드.
     * @param maxTarget 검증 조건 최대값
     * @param args 검증할 int 파라미터들
     * @return Int 값이 최대값보다 크면 true
     */
    public static boolean isMoreThan(int maxTarget, int... args) {
        for (int arg : args) {
            if (arg <= maxTarget) {
                return false;
            }
        }
        return true;
    }

    /**
     * Double 값이 특정 값보다 큰 값인지를 검증하는 메소드.
     * @param maxTarget 검증 조건 최대값
     * @param args 검증할 Double 파라미터들
     * @return Double 값이 최댓값보다 크면 true
     */
    public static boolean isMoreThan(double maxTarget, double... args) {
        for (double arg : args) {
            if (arg <= maxTarget) {
                return false;
            }
        }
        return true;
    }

    /**
     * 파라미터가 유효한 Hour 인지 검증하는 메소드.
     * @param args 검증할 Hour 파라미터
     * @return 유효한 Hour 이면 true
     */
    public static boolean isExactHour(int... args) {
        for (int arg : args) {
            if (arg < 0 || arg > 23) {
                return false;
            }
        }
        return true;
    }

    /**
     * 파라미터가 유효한 Minute 인지 검증하는 메소드.
     * @param args 검증할 Minute 파라미터
     * @return 유효한 Minute 이면 true
     */
    public static boolean isExactMinute(int... args) {
        for (int arg : args) {
            if (arg < 0 || arg > 59) {
                return false;
            }
        }
        return true;
    }

    /**
     * 요청 시간이 criteriaMinute 단위의 시간인지 검증하는 메소드. <br>
     * 예를 들어, 특정 시간이 5분 단위임을 검증하는 메소드다.
     * @param time 검증할 시간
     * @param criteriaMinute 검증 단위 시간 조건
     * @return 검증할 시간이 단위 시간에 부합하면 true
     */
    public static boolean isTimeInMinutes(LocalDateTime time, int criteriaMinute) {
        return time.getMinute() % criteriaMinute == 0;
    }

    /**
     * 요청 시간과 현재 시간의 차이가 특정 제한 분보다 작은지 검증하는 메소드. <br>
     * @param time 검증할 요청 시간
     * @param criteriaMinute 요청시간과 비교할 분
     * @return 요청시간과 현재 시간의 차이가 특정 분보다 작으면 true
     */
    public static boolean isDifferenceFromNowLessThanMinutes(LocalDateTime time, int criteriaMinute) {
        LocalDateTime now = SeoulDateTime.now();
        return now.isBefore(time) ?
                now.plusMinutes(criteriaMinute).isAfter(time) :
                now.minusMinutes(criteriaMinute).isBefore(time);
    }

    /**
     * 요청 시간이 현재 시간보다 이후인지를 검증하는 메소드.
     * @param time 검증할 요청 시간
     * @return 요청시간이 현재 시간보다 이후면 true
     */
    public static boolean isAfterNow(LocalDateTime time) {
        return time.isAfter(SeoulDateTime.now());
    }
}
