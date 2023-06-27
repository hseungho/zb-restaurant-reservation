package com.zerobase.hseungho.restaurantreservation.service.dto.user;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
public class TokenDto {

    private String accessToken;
    private String refreshToken;
    private LocalDateTime loggedInAt;

}
