package com.zerobase.hseungho.restaurantreservation.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CheckUsingResourceAvailable {

    @Getter
    @Setter
    @AllArgsConstructor(staticName = "of")
    public static class Response {
        private boolean result;

    }

}
