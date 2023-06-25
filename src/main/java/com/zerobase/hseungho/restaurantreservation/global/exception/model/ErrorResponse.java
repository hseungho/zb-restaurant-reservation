package com.zerobase.hseungho.restaurantreservation.global.exception.model;

import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private String timestamp;
    private HttpStatus httpStatus;
    private String errorCode;
    private String errorName;
    private String errorMessage;
    private String path;

    public static ErrorResponse errorResponse(ErrorCode errorCode, String path) {
        return ErrorResponse.of(
                SeoulDateTime.now().toString(),
                errorCode.getHttpStatus(),
                errorCode.getErrorCode(),
                errorCode.getErrorName(),
                errorCode.getErrorMessage(),
                path
        );
    }

    @Override
    public String toString() {
        return "{" +
                "timestamp=" + timestamp +
                ", httpStatus=" + httpStatus +
                ", errorCode='" + errorCode + '\'' +
                ", errorName='" + errorName + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
