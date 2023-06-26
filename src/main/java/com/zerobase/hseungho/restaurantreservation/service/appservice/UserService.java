package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.Login;
import com.zerobase.hseungho.restaurantreservation.service.dto.SignUp;
import com.zerobase.hseungho.restaurantreservation.service.dto.TokenDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.UserDto;

public interface UserService {

    /**
     * 아이디 중복확인 메소드.
     * @param userId 중복확인할 아이디
     * @return 사용가능하면 true, 아니라면 false
     */
    boolean checkUserIdAvailable(String userId);

    /**
     * 닉네임 중복확인 메소드.
     * @param nickname 중복확인할 닉네임
     * @return 사용가능하면 true, 아니라면 false
     */
    boolean checkNicknameAvailable(String nickname);

    /**
     * 회원가입 메소드.
     * @param request 회원가입 요청 DTO 클래스
     * @return 유저 DTO 클래스
     */
    UserDto signUp(SignUp.Request request);

    /**
     * 로그인 메소드.
     * @param request 로그인 요청 DTO 클래스
     * @return 토큰 DTO 클래스
     */
    TokenDto login(Login.Request request);

    /**
     * 유저 파트너 등록 메소드.
     * @return 유저 DTO 클래스
     */
    UserDto registerPartner();
}
