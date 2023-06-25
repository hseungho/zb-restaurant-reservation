package com.zerobase.hseungho.restaurantreservation.global.exception.base;

import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException implements ErrorCode {

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorName;
    private final String errorMessage;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.httpStatus = errorCode.getHttpStatus();
        this.errorCode = errorCode.getErrorCode();
        this.errorName = errorCode.getErrorName();
        this.errorMessage = errorCode.getErrorMessage();
    }


}
