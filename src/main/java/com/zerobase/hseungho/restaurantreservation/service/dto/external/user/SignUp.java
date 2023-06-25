package com.zerobase.hseungho.restaurantreservation.service.dto.external.user;

import com.zerobase.hseungho.restaurantreservation.service.dto.internal.user.UserDto;
import lombok.*;

public class SignUp {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String userId;
        private String password;
        private String nickname;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String userId;
        private String nickname;
        private String userType;
        private String createdAt;

        public static Response fromDto(UserDto dto) {
            return Response.builder()
                    .userId(dto.getUserId())
                    .nickname(dto.getNickname())
                    .userType(dto.getType().name())
                    .createdAt(dto.getCreatedAt().toString())
                    .build();
        }
    }
}
