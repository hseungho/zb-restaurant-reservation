package com.zerobase.hseungho.restaurantreservation.global.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodeType implements ErrorCode {

    UNAUTHORIZED_LOGIN_REQUESTED_VALUE(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 잘못 입력했습니다."),
    UNAUTHORIZED_LOGIN_ALREADY_RESIGNED_USER(HttpStatus.UNAUTHORIZED, "이미 탈퇴 처리된 아이디입니다."),
    UNAUTHORIZED_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "인증 토큰이 만료되었습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "인증 토큰이 유효하지 않습니다. 다시 로그인해주세요."),

    BAD_REQUEST_SIGN_UP_BLANK(HttpStatus.BAD_REQUEST, "회원가입은 아이디, 비밀번호, 닉네임 모두 입력해야 합니다."),
    BAD_REQUEST_SIGN_UP_USER_ID_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    BAD_REQUEST_SIGN_UP_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 영어, 숫자, 특수문자를 포함한 8자리 이상의 문자만 가능합니다."),
    BAD_REQUEST_SIGN_UP_NICKNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    BAD_REQUEST_SIGN_UP_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, "닉네임은 15자리 미만이어야 합니다."),
    BAD_REQUEST_PARTNER_ALREADY(HttpStatus.BAD_REQUEST, "이미 파트너 등록되어 있는 유저입니다."),
    BAD_REQUEST_SAVE_RESTAURANT_USER_NOT_PARTNER(HttpStatus.BAD_REQUEST, "파트너가 아닙니다. 파트너 등록을 먼저 해주세요."),
    BAD_REQUEST_SAVE_RESTAURANT_ALREADY_MANAGER(HttpStatus.BAD_REQUEST, "이미 등록한 매장이 있습니다. 매장은 하나만 등록 가능합니다."),
    BAD_REQUEST_SAVE_RESTAURANT_BLANK(HttpStatus.BAD_REQUEST, "매장 등록에 필요한 필수 정보를 모두 입력해주세요."),
    BAD_REQUEST_SAVE_RESTAURANT_COORDINATE_BY_ADDRESS(HttpStatus.BAD_REQUEST, "해당 주소의 좌표를 가져오는데에 실패하였습니다. 주소를 다시 시도해주세요."),

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),

    INTERNAL_SERVER_ERROR_KAKAO_API(HttpStatus.INTERNAL_SERVER_ERROR, "내부적으로 주소 좌표를 가져오는데에 실패하였습니다. 관리자에게 문의해주세요."),

    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final String errorCode = "R-" + "0".repeat(Math.max(4-String.valueOf(this.ordinal() + 1).length(), 0)) + (this.ordinal() + 1);

    ErrorCodeType(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    ErrorCodeType(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.errorMessage = "";
    }

    public String getErrorName() {
        return this.name();
    }

}
