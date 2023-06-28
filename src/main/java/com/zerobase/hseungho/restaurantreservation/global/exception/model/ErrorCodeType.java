package com.zerobase.hseungho.restaurantreservation.global.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodeType implements ErrorCode {

    //////////////////////////////////////////////////////////////////////////////
    UNAUTHORIZED_LOGIN_REQUESTED_VALUE(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 잘못 입력했습니다."),
    UNAUTHORIZED_LOGIN_ALREADY_RESIGNED_USER(HttpStatus.UNAUTHORIZED, "이미 탈퇴 처리된 아이디입니다."),

    UNAUTHORIZED_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "인증 토큰이 만료되었습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "인증 토큰이 유효하지 않습니다. 다시 로그인해주세요."),
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
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
    BAD_REQUEST_SEARCH_RESTAURANT_INVALID_VALUE(HttpStatus.BAD_REQUEST, "위치값이 유효하지 않습니다."),

    BAD_REQUEST_RESERVE_RESERVATION_BLANK(HttpStatus.BAD_REQUEST, "예약 요청에 필요한 모든 정보를 입력해주세요."),
    BAD_REQUEST_RESERVE_RESERVATION_RESERVING_CANNOT_MANAGER(HttpStatus.BAD_REQUEST, "매장의 점장은 예약 요청할 수 없습니다."),
    BAD_REQUEST_RESERVE_RESERVATION_RESERVED_TIME_IS_INVALID_RESTAURANT_TIME(HttpStatus.BAD_REQUEST, "매장의 오픈 및 마감 시간에 맞게 요청해주세요."),
    BAD_REQUEST_RESERVE_RESERVATION_RESERVED_TIME_IS_NOT_IN_FIVE_MINUTES(HttpStatus.BAD_REQUEST, "예약시간은 5분 단위로 요청해야 합니다."),
    BAD_REQUEST_RESERVE_RESERVATION_RESERVED_TIME_CANNOT_LESS_THAN_TEN_MINUTES(HttpStatus.BAD_REQUEST, "현재시간으로부터 10분 후의 시간은 예약할 수 없습니다."),
    BAD_REQUEST_RESERVE_RESERVATION_NUM_OF_PERSON_OVER_MAX_PER_RESERVATION(HttpStatus.BAD_REQUEST,"예약 요청 인원이 매장에서 설정한 최대 인원에 맞지 않습니다."),
    BAD_REQUEST_RESERVE_RESERVATION_RESERVED_TIME_IS_FULL(HttpStatus.BAD_REQUEST, "해당 예약 시간의 예약 수가 이미 만석입니다."),
    BAD_REQUEST_RESERVE_RESERVATION_RESERVED_TIME_IS_BEFORE_NOW(HttpStatus.BAD_REQUEST, "현재시간보다 이전 시간은 예약할 수 없습니다."),
    BAD_REQUEST_RESERVE_RESERVATION_REQ_DATE_AFTER_DELETED_RESTAURANT(HttpStatus.BAD_REQUEST, "해당 예약시간은 매장 사정으로 인해 불가능합니다."),

    BAD_REQUEST_CANCEL_RESERVATION_DELETED_RESTAURANT(HttpStatus.BAD_REQUEST, "영업 종료된 매장의 예약은 취소할 수 없습니다."),
    BAD_REQUEST_CANCEL_RESERVATION_CANCELED_TIME_CANNOT_LESS_THAN_THIRTY_MINUTES(HttpStatus.BAD_REQUEST, "예약 30분 전에는 예약을 취소할 수 없습니다."),
    BAD_REQUEST_CANCEL_RESERVATION_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "이미 취소된 예약입니다."),

    BAD_REQUEST_APPROVE_RESERVATION_DELETED_RESTAURANT(HttpStatus.BAD_REQUEST, "영업 종료된 매장의 예약은 승인할 수 없습니다."),
    BAD_REQUEST_APPROVE_RESERVATION_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "이미 취소된 예약입니다."),
    BAD_REQUEST_APPROVE_RESERVATION_ALREADY_APPROVED(HttpStatus.BAD_REQUEST, "이미 승인된 예약입니다."),
    BAD_REQUEST_APPROVE_RESERVATION_STATUS_IS_NOT_SUITED_APPROVE(HttpStatus.BAD_REQUEST, "승인할 수 없는 예약 상태입니다."),

    BAD_REQUEST_REFUSE_RESERVATION_DELETED_RESTAURANT(HttpStatus.BAD_REQUEST, "영업 종료된 매장의 예약은 거절할 수 없습니다."),
    BAD_REQUEST_REFUSE_RESERVATION_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "이미 취소된 예약입니다."),
    BAD_REQUEST_REFUSE_RESERVATION_ALREADY_REFUSED(HttpStatus.BAD_REQUEST, "이미 거절된 예약입니다."),
    BAD_REQUEST_REFUSE_RESERVATION_STATUS_IS_NOT_SUITED_REFUSE(HttpStatus.BAD_REQUEST, "승인할 수 없는 예약 상태입니다."),
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    FORBIDDEN_CANCEL_RESERVATION_NOT_YOUR_RESOURCE(HttpStatus.FORBIDDEN, "다른 고객의 예약을 취소할 수 없습니다."),

    FORBIDDEN_APPROVE_RESERVATION_CUSTOMER_CANNOT_APPROVE(HttpStatus.FORBIDDEN, "파트너가 아닌 유저는 예약을 승인할 수 없습니다."),
    FORBIDDEN_APPROVE_RESERVATION_NOT_MANAGER_OF_RESTAURANT(HttpStatus.FORBIDDEN, "매장의 점장이 아닌 유저는 예약을 승인할 수 없습니다."),

    FORBIDDEN_REFUSE_RESERVATION_CUSTOMER_CANNOT_REFUSE(HttpStatus.FORBIDDEN, "파트너가 아닌 유저는 예약을 거절할 수 없습니다."),
    FORBIDDEN_REFUSE_RESERVATION_NOT_MANAGER_OF_RESTAURANT(HttpStatus.FORBIDDEN, "매장의 점장이 아닌 유저는 예약을 거절할 수 없습니다."),
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_FOUND_RESTAURANT(HttpStatus.NOT_FOUND, "존재하지 않는 매장입니다."),
    NOT_FOUND_RESERVATION(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    INTERNAL_SERVER_ERROR_KAKAO_API(HttpStatus.INTERNAL_SERVER_ERROR, "내부적으로 주소 좌표를 가져오는데에 실패하였습니다. 관리자에게 문의해주세요."),
    INTERNAL_SERVER_ERROR_GENERATE_RESERVATION_NUMBER(HttpStatus.INTERNAL_SERVER_ERROR, "예약번호를 생성하는데에 문제가 발생하였습니다. 관리자에게 문의해주세요."),
    //////////////////////////////////////////////////////////////////////////////

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
