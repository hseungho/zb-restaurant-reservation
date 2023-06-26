package com.zerobase.hseungho.restaurantreservation.global.constants;

import org.springframework.http.HttpHeaders;

public class TokenConstants {

    public static final String CLAIMS_ROLE = "role";
    public static final long ACCESS_TOKEN_EXPIRED_TIME = 1000 * 60 * 60;
    public static final long REFRESH_TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 24 * 30L;
    public static final String TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
    public static final String TOKEN_PREFIX = "Bearer ";

}
