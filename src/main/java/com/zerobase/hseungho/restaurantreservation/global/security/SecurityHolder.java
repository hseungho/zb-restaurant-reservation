package com.zerobase.hseungho.restaurantreservation.global.security;

import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHolder {

    public static String getIdOfUser() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

}
