package com.zerobase.hseungho.restaurantreservation.service.appservice;

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

}
