package com.zerobase.hseungho.restaurantreservation.global.constants;

import java.util.List;

public class PermitApiConstants {
    public static final List<String> PERMIT_APIS = List.of(
            "/**/sign-up/**", "/**/login/**", "/**/search/**", "/h2-console/**"
    );
    public static final String[] PERMIT_APIS_TO_ARRAY = PERMIT_APIS.toArray(String[]::new);
}
