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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String accessToken;
        private String refreshToken;
        private String loggedInAt;

        public static Response fromTokenDto(TokenDto dto) {
            return Response.builder()
                    .accessToken(dto.getAccessToken())
                    .refreshToken(dto.getRefreshToken())
                    .loggedInAt(dto.getLoggedInAt().toString())
                    .build();
        }
    }
}
