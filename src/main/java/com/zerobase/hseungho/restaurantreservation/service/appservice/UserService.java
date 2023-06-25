package com.zerobase.hseungho.restaurantreservation.service.appservice;

public interface UserService {

    /**
     * 아이디 중복확인 메소드.
     * @param userId 중복확인할 아이디
     * @return 사용가능하면 true, 사용불가면 false
     */
    boolean checkIdAvailable(String userId);
    
}
