package com.zerobase.hseungho.restaurantreservation.global.util;

import java.util.Random;
import java.util.UUID;

/**
 * 필요한 목적에 따라 값을 생성하는 생성기 유틸리티 클래스.
 */
public class Generator {

    /**
     * UUID 를 생성하여 반환하는 메소드.
     * @return 생성된 UUID
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 예약번호를 생성하여 반환하는 메소드. <br>
     * @return 영어 대문자와 숫자를 혼합하여 생성된 10자리 예약번호
     */
    public static String generateReservationNumber() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            if (random.nextBoolean()) {
                sb.append((char)(random.nextInt(26) + 65));
            } else {
                sb.append(random.nextInt(10));
            }
        }
        return sb.toString();
    }

    /**
     * 탈퇴하는 유저의 닉네임을 생성하여 반환하는 메소드.
     * @return 생성된 탈퇴 유저 닉네임
     */
    public static String generateDelUserNickname() {
        return "del" + generateRandomNum(6);
    }

    /**
     * 주어진 길이에 따라 랜덤된 숫자를 생성하여 반환하는 메소드.
     * @param length 랜덤 숫자 문자열 길이
     * @return 생성된 랜덤 숫자 문자열
     */
    public static String generateRandomNum(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
