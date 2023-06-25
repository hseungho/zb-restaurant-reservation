package com.zerobase.hseungho.restaurantreservation.service.dto.external.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SignUp {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String userId;
        private String password;
        private String nickname;
    }

    public static class Response {

    }
}
