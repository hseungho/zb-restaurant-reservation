package com.zerobase.hseungho.restaurantreservation.global.exception.impl;

import com.zerobase.hseungho.restaurantreservation.global.exception.base.BaseException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCode;

public class InternalServerErrorException extends BaseException {
    public InternalServerErrorException(ErrorCode errorCode) {
        super(errorCode);
    }
}
