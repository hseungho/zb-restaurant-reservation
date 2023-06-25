package com.zerobase.hseungho.restaurantreservation.global.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodeType implements ErrorCode {

    BAD_REQUEST_SIGN_UP_USER_ID_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    BAD_REQUEST_SIGN_UP_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 영어, 숫자, 특수문자를 포함한 8자리 이상의 문자만 가능합니다."),
    BAD_REQUEST_SIGN_UP_NICKNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    BAD_REQUEST_SIGN_UP_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, "닉네임은 15자리 미만이어야 합니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
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
