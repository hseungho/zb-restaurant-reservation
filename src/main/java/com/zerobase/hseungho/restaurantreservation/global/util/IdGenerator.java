package com.zerobase.hseungho.restaurantreservation.global.util;

import java.util.Random;
import java.util.UUID;

public class IdGenerator {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateReservationNumber() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            if (random.nextBoolean()) {
                sb.append((char)(random.nextInt(26) +97));
            } else {
                sb.append(random.nextInt(10));
            }
        }
        return sb.toString();
    }

}
