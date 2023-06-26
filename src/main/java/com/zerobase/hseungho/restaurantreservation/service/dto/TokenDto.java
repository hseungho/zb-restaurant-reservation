package com.zerobase.hseungho.restaurantreservation.service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

    private String accessToken;
    private String refreshToken;
    private LocalDateTime loggedInAt;

}
