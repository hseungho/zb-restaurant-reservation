package com.zerobase.hseungho.restaurantreservation.global.util;

import java.util.UUID;

public class IdGenerator {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
