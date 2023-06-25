package com.zerobase.hseungho.restaurantreservation.service.dto.external.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CheckIdAvailable {

    @Getter
    @Setter
    @AllArgsConstructor(staticName = "of")
    public static class Response {
        private boolean result;

    }

}
