package com.zerobase.hseungho.restaurantreservation.global.exception;

import com.zerobase.hseungho.restaurantreservation.global.exception.base.BaseException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(BaseException ex, HttpServletRequest request) {
        return ResponseEntity.status(ex.getHttpStatus())
                .body(ErrorResponse.errorResponse(ex, request.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        log.error("Occurred AccessDeniedException -> ", ex);
        ErrorCodeType errorCode = ErrorCodeType.FORBIDDEN;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.errorResponse(errorCode, request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleServerException(Exception ex, HttpServletRequest request) {
        log.error("Occurred Server Exception -> ", ex);
        ErrorCodeType errorCode = ErrorCodeType.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.errorResponse(errorCode, request.getRequestURI()));
    }

}
