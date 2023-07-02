package com.zerobase.hseungho.restaurantreservation.global.exception.base;

import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException implements ErrorCode {

    private final ErrorCode errorCodeType;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorName;
    private final String errorMessage;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCodeType = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
        this.errorCode = errorCode.getErrorCode();
        this.errorName = errorCode.getErrorName();
        this.errorMessage = errorCode.getErrorMessage();
    }

    public BaseException(ErrorCode errorCode, String msg) {
        super(errorCode.getErrorMessage()+" "+msg);
        this.errorCodeType = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
        this.errorCode = errorCode.getErrorCode();
        this.errorName = errorCode.getErrorName();
        this.errorMessage = errorCode.getErrorMessage() + " " + msg;
    }


}
