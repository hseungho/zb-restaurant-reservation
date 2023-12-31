package com.zerobase.hseungho.restaurantreservation.service.dto.user;

import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String userId;
    private String nickname;
    private UserType type;
    private LocalDateTime loggedInAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static UserDto empty() {
        return new UserDto();
    }

    public static UserDto fromEntity(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .nickname(entity.getNickname())
                .type(entity.getType())
                .loggedInAt(entity.getLoggedInAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

}
