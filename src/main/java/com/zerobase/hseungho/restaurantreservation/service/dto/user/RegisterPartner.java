package com.zerobase.hseungho.restaurantreservation.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RegisterPartner {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String userId;
        private String nickname;
        private String userType;
        public static Response fromDto(UserDto dto) {
            return Response.builder()
                    .userId(dto.getUserId())
                    .nickname(dto.getNickname())
                    .userType(dto.getType().name())
                    .build();
        }
    }
}
