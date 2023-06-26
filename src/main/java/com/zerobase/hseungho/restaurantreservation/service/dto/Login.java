package com.zerobase.hseungho.restaurantreservation.service.dto;

import lombok.*;

public class Login {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String userId;
        private String password;
    }
}
