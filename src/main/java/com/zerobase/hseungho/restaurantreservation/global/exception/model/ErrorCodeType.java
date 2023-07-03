package com.zerobase.hseungho.restaurantreservation.global.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodeType implements ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근할 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "리소스가 존재하지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "API 서버가 정상적인 응답이 불가능한 상태입니다. 관리자에게 바로 문의해주세요."),

    //////////////////////////////////////////////////////////////////////////////
    UNAUTHORIZED_LOGIN_REQUESTED_VALUE(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 잘못 입력했습니다."),
    UNAUTHORIZED_LOGIN_ALREADY_RESIGNED_USER(HttpStatus.UNAUTHORIZED, "회원탈퇴된 유저입니다."),

    UNAUTHORIZED_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "인증 토큰이 만료되었습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "인증 토큰이 유효하지 않습니다. 다시 로그인해주세요."),

    UNAUTHORIZED_REFRESH_TOKEN_BLANK(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 없습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다. 다시 로그인해주세요."),

    UNAUTHORIZED_UPDATE_PASSWORD_INVALID_CUR_PASSWORD(HttpStatus.UNAUTHORIZED, "현재 비밀번호가 일치하지 않습니다."),
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    BAD_REQUEST_SIGN_UP_BLANK(HttpStatus.BAD_REQUEST, "회원가입은 아이디, 비밀번호, 닉네임 모두 입력해야 합니다."),
    BAD_REQUEST_SIGN_UP_USER_ID_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    BAD_REQUEST_SIGN_UP_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 영어, 숫자, 특수문자를 포함한 8자리 이상의 문자만 가능합니다."),
    BAD_REQUEST_SIGN_UP_NICKNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    BAD_REQUEST_SIGN_UP_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, "닉네임은 15자리 미만이어야 합니다."),

    BAD_REQUEST_UPDATE_PASSWORD_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 영어, 숫자, 특수문자를 포함한 8자리 이상의 문자만 가능합니다."),

    BAD_REQUEST_UPDATE_PROFILE_BLANK(HttpStatus.BAD_REQUEST, "수정할 정보를 모두 입력해주세요."),
    BAD_REQUEST_UPDATE_PROFILE_NICKNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    BAD_REQUEST_UPDATE_PROFILE_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, "닉네임은 15자리 미만이어야 합니다."),
    BAD_REQUEST_UPDATE_PROFILE_SAME_NICKNAME(HttpStatus.BAD_REQUEST, "이미 설정된 닉네임으로 수정할 수 없습니다."),

    BAD_REQUEST_PARTNER_ALREADY(HttpStatus.BAD_REQUEST, "이미 파트너 등록되어 있는 유저입니다."),

    BAD_REQUEST_RESIGN_ALREADY(HttpStatus.BAD_REQUEST, "이미 탈퇴한 유저입니다."),
    BAD_REQUEST_RESIGN_HAVING_RESTAURANT(HttpStatus.BAD_REQUEST, "매장을 소유하고 있는 파트너 유저는 탈퇴할 수 없습니다."),

    BAD_REQUEST_SAVE_RESTAURANT_ALREADY_MANAGER(HttpStatus.BAD_REQUEST, "이미 등록한 매장이 있습니다. 매장은 하나만 등록 가능합니다."),
    BAD_REQUEST_SAVE_RESTAURANT_BLANK(HttpStatus.BAD_REQUEST, "매장 등록에 필요한 필수 정보를 모두 입력해주세요."),
    BAD_REQUEST_SAVE_RESTAURANT_COORDINATE_BY_ADDRESS(HttpStatus.BAD_REQUEST, "해당 주소의 좌표를 가져오는데에 실패하였습니다. 주소를 다시 시도해주세요."),

    BAD_REQUEST_UPDATE_RESTAURANT_BLANK(HttpStatus.BAD_REQUEST, "매장 정보 수정에 필요한 모든 정보를 입력해주세요."),

    BAD_REQUEST_DELETE_RESTAURANT_REMAIN_RESERVATION(HttpStatus.BAD_REQUEST, "매장에 예약이 남아있어서 매장을 삭제할 수 없습니다."),
    BAD_REQUEST_DELETE_RESTAURANT_ALREADY(HttpStatus.BAD_REQUEST, "이미 삭제된 매장입니다."),
    BAD_REQUEST_REQUEST_DELETING_RESTAURANT_REMAIN_RESERVATION(HttpStatus.BAD_REQUEST, "해당 일자에 매장에 예약이 남아있어서 매장을 삭제 요청할 수 없습니다."),
    BAD_REQUEST_REQUEST_DELETING_RESTAURANT_ALREADY(HttpStatus.BAD_REQUEST, "이미 삭제된 매장입니다."),
    BAD_REQUEST_REQUEST_DELETING_RESTAURANT_REQ_TIME_IS_BEFORE_NOW(HttpStatus.BAD_REQUEST, "현재 시간보다 이전 시간에 매장을 삭제 요청할 수 없습니다."),

    BAD_REQUEST_SEARCH_RESTAURANT_INVALID_VALUE(HttpStatus.BAD_REQUEST, "위치값이 유효하지 않습니다."),
    BAD_REQUEST_SEARCH_RESTAURANT_INVALID_SORT_PROPERTY(HttpStatus.BAD_REQUEST, "유효하지 않은 정렬 속성입니다."),

    BAD_REQUEST_FIND_RESTAURANT_DELETE_RESTAURANT(HttpStatus.BAD_REQUEST, "영업 종료된 매장입니다."),

    BAD_REQUEST_ADD_MENUS_BLANK(HttpStatus.BAD_REQUEST, "메뉴 추가에 필요한 모든 정보를 입력해주세요."),
    BAD_REQUEST_ADD_MENUS_DELETE_RESTAURANT(HttpStatus.BAD_REQUEST, "영업 종료된 매장의 메뉴를 추가할 수 없습니다."),
    BAD_REQUEST_UPDATE_MENU_BLANK(HttpStatus.BAD_REQUEST, "메뉴 수정에 필요한 모든 정보를 입력해주세요."),
    BAD_REQUEST_UPDATE_MENU_DELETE_RESTAURANT(HttpStatus.BAD_REQUEST, "영업 종료된 매장의 메뉴를 수정할 수 없습니다."),
    BAD_REQUEST_REMOVE_MENU_DELETE_RESTAURANT(HttpStatus.BAD_REQUEST, "영업 종료된 매장의 메뉴를 삭제할 수 없습니다."),

    BAD_REQUEST_RESERVE_RESERVATION_BLANK(HttpStatus.BAD_REQUEST, "예약 요청에 필요한 모든 정보를 입력해주세요."),
    BAD_REQUEST_RESERVE_RESERVATION_DELETED_RESTAURANT(HttpStatus.BAD_REQUEST, "영업 종료된 매장입니다."),
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

    BAD_REQUEST_VISIT_RESERVATION_DELETED_RESTAURANT(HttpStatus.BAD_REQUEST, "영업 종료된 매장에 도착확인할 수 없습니다."),
    BAD_REQUEST_VISIT_RESERVATION_VISITED_TIME_CANNOT_LESS_THAN_TEN_MINUTES(HttpStatus.BAD_REQUEST, "예약시간의 10분 전부터는 도착확인할 수 없습니다."),
    BAD_REQUEST_VISIT_RESERVATION_VISIT_CAN_THIRTY_MINUTES_BEFORE_RESERVED_AT(HttpStatus.BAD_REQUEST, "도착확인은 에약시간 30분 전부터 가능합니다."),
    BAD_REQUEST_VISIT_RESERVATION_ALREADY_VISITED(HttpStatus.BAD_REQUEST, "이미 도착확인된 예약입니다."),
    BAD_REQUEST_VISIT_RESERVATION_RESERVED_STATUS_CANNOT_VISIT(HttpStatus.BAD_REQUEST, "도착확인하기 위해서는 점장이 예약을 승인해야 합니다."),
    BAD_REQUEST_VISIT_RESERVATION_STATUS_IS_NOT_SUITED_VISIT(HttpStatus.BAD_REQUEST, "도착확인할 수 없는 예약 상태입니다."),

    BAD_REQUEST_FIND_RESERVATION_DELETED_RESTAURANT(HttpStatus.BAD_REQUEST, "영업 종료된 매장의 예약은 조회할 수 없습니다."),

    BAD_REQUEST_SAVE_REVIEW_INVALID_RATING_RANGE(HttpStatus.BAD_REQUEST, "평점은 1~5점 사이의 점수만 줄 수 있습니다."),
    BAD_REQUEST_SAVE_REVIEW_BLANK(HttpStatus.BAD_REQUEST, "리뷰 등록에 필요한 모든 정보를 입력해주세요."),
    BAD_REQUEST_SAVE_REVIEW_DELETED_RESTAURANT(HttpStatus.BAD_REQUEST, "영업 종료된 매장에 리뷰를 등록할 수 없습니다."),
    BAD_REQUEST_SAVE_REVIEW_CANNOT_BEFORE_RESERVED_TIME(HttpStatus.BAD_REQUEST, "예약시간 이전에 리뷰를 등록할 수 없습니다."),
    BAD_REQUEST_SAVE_REVIEW_CAN_ONLY_VISITED(HttpStatus.BAD_REQUEST, "도착확인이 되기 전에 리뷰를 등록할 수 없습니다."),
    BAD_REQUEST_SAVE_REVIEW_ALREADY_REVIEWER(HttpStatus.BAD_REQUEST, "이미 해당 예약에 대한 리뷰를 등록하였습니다."),

    BAD_REQUEST_UPDATE_REVIEW_INVALID_RATING_RANGE(HttpStatus.BAD_REQUEST, "평점은 1~5점 사이의 점수만 줄 수 있습니다."),
    BAD_REQUEST_UPDATE_REVIEW_BLANK(HttpStatus.BAD_REQUEST, "리뷰 수정에 필요한 모든 정보를 입력해주세요."),
    BAD_REQUEST_UPDATE_REVIEW_DELETED_RESTAURANT(HttpStatus.BAD_REQUEST, "영업 종료된 매장의 리뷰를 수정할 수 없습니다."),

    BAD_REQUEST_DELETE_REVIEW_DELETED_RESTAURANT(HttpStatus.BAD_REQUEST, "영업 종료된 매장의 리뷰를 삭제할 수 없습니다."),

    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    FORBIDDEN_ONLY_PARTNER(HttpStatus.FORBIDDEN, "파트너 유저만 접근할 수 있는 API 입니다."),

    FORBIDDEN_UPDATE_RESTAURANT_NOT_YOUR_RESTAURANT(HttpStatus.FORBIDDEN, "매장의 점장이 아닌 유저는 매장 정보를 수정할 수 없습니다."),
    FORBIDDEN_DELETE_RESTAURANT_NOT_YOUR_RESTAURANT(HttpStatus.FORBIDDEN, "매장의 점장이 아닌 유저는 매장을 삭제할 수 없습니다."),
    FORBIDDEN_REQUEST_DELETING_RESTAURANT_NOT_YOUR_RESTAURANT(HttpStatus.FORBIDDEN, "매장의 점장이 아닌 유저는 매장을 삭제 요청할 수 없습니다."),

    FORBIDDEN_ADD_MENUS_NOT_YOUR_RESTAURANT(HttpStatus.FORBIDDEN, "매장의 점장이 아닌 유저는 메뉴를 추가할 수 없습니다."),
    FORBIDDEN_UPDATE_MENU_NOT_YOUR_RESTAURANT(HttpStatus.FORBIDDEN, "매장의 점장이 아닌 유저는 메뉴를 수정할 수 없습니다."),
    FORBIDDEN_REMOVE_MENU_NOY_YOUR_RESTAURANT(HttpStatus.FORBIDDEN, "매장의 점장이 아닌 유저는 메뉴를 삭제할 수 없습니다."),

    FORBIDDEN_CANCEL_RESERVATION_NOT_YOUR_RESOURCE(HttpStatus.FORBIDDEN, "다른 고객의 예약을 취소할 수 없습니다."),

    FORBIDDEN_APPROVE_RESERVATION_CUSTOMER_CANNOT_APPROVE(HttpStatus.FORBIDDEN, "파트너가 아닌 유저는 예약을 승인할 수 없습니다."),
    FORBIDDEN_APPROVE_RESERVATION_NOT_MANAGER_OF_RESTAURANT(HttpStatus.FORBIDDEN, "매장의 점장이 아닌 유저는 예약을 승인할 수 없습니다."),

    FORBIDDEN_REFUSE_RESERVATION_CUSTOMER_CANNOT_REFUSE(HttpStatus.FORBIDDEN, "파트너가 아닌 유저는 예약을 거절할 수 없습니다."),
    FORBIDDEN_REFUSE_RESERVATION_NOT_MANAGER_OF_RESTAURANT(HttpStatus.FORBIDDEN, "매장의 점장이 아닌 유저는 예약을 거절할 수 없습니다."),

    FORBIDDEN_VISIT_RESERVATION_NOT_MANAGER_OF_RESTAURANT(HttpStatus.FORBIDDEN, "매장의 점장이 아닌 유저가 도착확인할 수 없습니다."),
    FORBIDDEN_VISIT_RESERVATION_NOT_YOUR_RESOURCE(HttpStatus.FORBIDDEN, "다른 고객의 예약을 도착확인할 수 없습니다."),

    FORBIDDEN_FIND_RESERVATION_LIST_ONLY_CLIENT(HttpStatus.FORBIDDEN, "고객이 아닌 유저는 고객의 예약 리스트를 조회할 수 없습니다."),
    FORBIDDEN_FIND_RESERVATION_LIST_ONLY_MANAGER(HttpStatus.FORBIDDEN, "파트너가 아닌 유저는 매장의 예약 리스트를 조회할 수 없습니다."),

    FORBIDDEN_FIND_RESERVATION_MANAGER_NOT_YOUR_RESOURCE(HttpStatus.FORBIDDEN, "해당 예약 매장의 점장이 아닙니다."),
    FORBIDDEN_FIND_RESERVATION_CLIENT_NOT_YOUR_RESOURCE(HttpStatus.FORBIDDEN, "해당 예약의 예약자가 아닙니다."),

    FORBIDDEN_SAVE_REVIEW_NOT_YOUR_RESOURCE(HttpStatus.FORBIDDEN, "해당 예약의 예약자가 아닙니다."),
    FORBIDDEN_UPDATE_REVIEW_NOT_YOUR_REVIEW(HttpStatus.FORBIDDEN, "작성자가 아닌 유저가 리뷰를 수정할 후 없습니다."),
    FORBIDDEN_DELETE_REVIEW_NOT_YOUR_REVIEW(HttpStatus.FORBIDDEN, "작성자가 아닌 유저가 리뷰를 삭제할 수 없습니다."),
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_FOUND_RESTAURANT(HttpStatus.NOT_FOUND, "존재하지 않는 매장입니다."),
    NOT_FOUND_RESERVATION(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
    NOT_FOUND_MENU(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴입니다."),
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    INTERNAL_SERVER_ERROR_GENERATE_DEL_USER_NICKNAME(HttpStatus.INTERNAL_SERVER_ERROR, "탈퇴 유저 닉네임을 생성하는데에 문제가 발생하였습니다. 관리자에게 문의해주세요."),
    INTERNAL_SERVER_ERROR_KAKAO_API(HttpStatus.INTERNAL_SERVER_ERROR, "내부적으로 주소 좌표를 가져오는데에 실패하였습니다. 관리자에게 문의해주세요."),
    INTERNAL_SERVER_ERROR_GENERATE_RESERVATION_NUMBER(HttpStatus.INTERNAL_SERVER_ERROR, "예약번호를 생성하는데에 문제가 발생하였습니다. 관리자에게 문의해주세요."),
    INTERNAL_SERVER_ERROR_UPLOAD_IMAGE_S3(HttpStatus.INTERNAL_SERVER_ERROR, "내부적으로 이미지를 업로드하는데에 문제가 발생했습니다. 관리자에게 문의해주세요."),
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
