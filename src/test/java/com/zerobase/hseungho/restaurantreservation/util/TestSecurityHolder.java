package com.zerobase.hseungho.restaurantreservation.util;

import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class TestSecurityHolder {
    public static User setSecurityHolderUser(UserType type) {
        User user = User.builder()
                .userId("testid")
                .password("testpassword1234!")
                .nickname("testnickname")
                .type(type)
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities())
        );
        return user;
    }
}
