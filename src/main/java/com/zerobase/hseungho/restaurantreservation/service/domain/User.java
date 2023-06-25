package com.zerobase.hseungho.restaurantreservation.service.domain;

import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "user")
@Getter
@NoArgsConstructor
public class User {

    @Id
    private String id;
    private String userId;
    private String password;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private UserType type;
    private LocalDateTime loggedInAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
