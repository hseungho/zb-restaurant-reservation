package com.zerobase.hseungho.restaurantreservation.service.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UpdateProfile {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank
        private String nickname;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String userId;
        private String nickname;
        private String userType;
        private String createdAt;
        private String updatedAt;
        public static Response fromDto(UserDto dto) {
            return Response.builder()
                    .userId(dto.getUserId())
                    .nickname(dto.getNickname())
                    .userType(dto.getType().name())
                    .createdAt(dto.getCreatedAt().toString())
                    .updatedAt(dto.getUpdatedAt().toString())
                    .build();
        }
    }
}
