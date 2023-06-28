package com.zerobase.hseungho.restaurantreservation.global.exception.impl;

import com.zerobase.hseungho.restaurantreservation.global.exception.base.BaseException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends BaseException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
