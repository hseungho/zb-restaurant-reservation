package com.zerobase.hseungho.restaurantreservation.global.security;

import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * SecurityHolder 는 SecurityContextHolder 에 담긴 유저 정보를 반환하기 위한 클래스.
 */
public class SecurityHolder {

    /**
     * SecurityContextHolder 에 담긴 유저 정보 중 PK ID를 반환하는 메소드.
     * @return 유저의 PK ID
     */
    public static String getIdOfUser() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    /**
     * SecurityContextHolder 에 담긴 유저 객체를 반환하는 메소드.
     * @return 유저 객체
     */
    public static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
