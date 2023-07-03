package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.user.*;

public interface UserService {

    /**
     * 아이디 사용가능 여부 확인 메소드. <br>
     * <br>
     * DB로부터 해당 아이디가 존재하는지에 대한 응답값을 반환한다. <br>
     * <br>
     * @param userId 중복확인할 아이디
     * @return 아이디가 사용가능하면 true
     */
    boolean checkUserIdAvailable(String userId);

    /**
     * 닉네임 사용가능 여부 확인 메소드. <br>
     * <br>
     * DB로부터 해당 닉네임이 존재하는지에 대한 응답값을 반환한다. <br>
     * <br>
     * @param nickname 중복확인할 닉네임
     * @return 닉네임이 사용가능하면 true
     */
    boolean checkNicknameAvailable(String nickname);

    /**
     * 회원가입 메소드. <br>
     * <br>
     * 새로운 유저를 저장하는 메소드로, 회원가입 로직에 대한 검증 로직을 거친 후, <br>
     * 새 유저 인스턴스를 DB에 저장한다. <br>
     * <br>
     * @param request 회원가입 요청 DTO 클래스
     * @return 유저 DTO 클래스
     */
    UserDto signUp(SignUp.Request request);

    /**
     * 로그인 메소드. <br>
     * <br>
     * 가입되어 있는 유저에 한하여 로그인 요청에 대한 JWT 토큰 값을 반환한다. <br>
     * 요청 정보의 유저 ID를 기준으로 DB에서 유저 정보를 조회하고, <br>
     * 로그인에 대한 검증 로직을 거친 후, 유저의 최근 로그인일시를 현재시간으로 변경한 뒤, <br>
     * JWT 토큰을 생성하여 반환한다. <br>
     * <br>
     * @param request 로그인 요청 DTO 클래스
     * @return 토큰 DTO 클래스
     */
    TokenDto login(Login.Request request);

    /**
     * 유저 파트너 등록 메소드. <br>
     * <br>
     * 파트너만이 매장을 등록시킬 수 있는 정책에 따라 로그인 유저 중 고객들에 한하여 <br>
     * 파트너로 변경하는 메소드다. <br>
     * <br>
     * @return 유저 DTO 클래스
     */
    UserDto registerPartner();

    /**
     * 액세스 토큰 리프레시 메소드. <br>
     * <br>
     * 액세스 토큰이 만료될 경우, 로그인 API 호출 없이 리프레시 토큰으로 액세스 토큰을 <br>
     * 재갱신해주는 메소드다. <br>
     * <br>
     * 리프레시 토큰을 검증하고, 해당 유저에 대한 정보를 토대로 액세스 토큰 및 리프레시 토큰을 <br>
     * 재발급하여 반환한다 .<br>
     * <br>
     * @param refreshToken 리프레시 토큰
     * @return 토큰 DTO 클래스
     */
    TokenDto refreshToken(String refreshToken);

    /**
     * 비밀번호 변경 메소드. <br>
     * <br>
     * 유저의 비밀번호를 변경하는 메소드로, 비밀번호 변경에 대한 검증 로직을 거친 후, <br>
     * 비밀번호를 변경하여 저장한다. <br>
     * <br>
     * @param request 비밀번호 변경 요청 DTO 클래스
     * @return 유저 DTO 클래스
     */
    UserDto updatePassword(UpdatePassword.Request request);

    /**
     * 내 정보 조회 메소드. <br>
     * <br>
     * 로그인 유저의 내 정보를 조회하는 메소드다. <br>
     * <br>
     * @return 유저 DTO 클래스
     */
    UserDto findProfile();

    /**
     * 내 정보 수정 메소드. <br>
     * <br>
     * 로그인 유저의 내 정보를 수정하는 메소드로, 내정보 수정에 대한 검증 로직을 거친 후, <br>
     * 해당 유저의 정보를 변경한다. <br>
     * <br>
     * @param request 정보 수정 요청 DTO 클래스
     * @return 유저 DTO 클래스
     */
    UserDto updateProfile(UpdateProfile.Request request);

    /**
     * 회원 탈퇴 메소드. <br>
     * <br>
     * 로그인 유저의 회원 탈퇴 요청 메소드로, 탈퇴에 대한 검증 로직을 거친 후, <br>
     * 유저의 탈퇴 일시를 현재시간으로 변경하여 저장한다. <br>
     * <br>
     * 유저의 정보를 실제 DB에서 삭제하는 것이 아닌 Soft Delete 방식으로, <br>
     * 해당 유저의 삭제 일시를 현재 시간으로 변경하고, <br>
     * 유저 타입을 탈퇴 유저로, 비밀번호를 null로, 닉네임을 탈퇴 유저 닉네임으로 변경한다. <br>
     * <br>
     * @return 유저 DTO 클래스
     */
    UserDto resign();
}
