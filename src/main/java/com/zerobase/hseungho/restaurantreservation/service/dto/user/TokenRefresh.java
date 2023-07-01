package com.zerobase.hseungho.restaurantreservation.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TokenRefresh {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String accessToken;
        private String refreshToken;
        public static Response fromTokenDto(TokenDto dto) {
            return Response.builder()
                    .accessToken(dto.getAccessToken())
                    .refreshToken(dto.getRefreshToken())
                    .build();
        }
    }
}
