package com.zerobase.hseungho.restaurantreservation.global.security;

import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * 요청이 Spring DispatchServlet 에 접근하기 이전에 AccessDeniedException 이 발생했을 경우, <br>
 * 적절한 에러 모델을 요청자에게 응답하기 위한 AccessDeniedHandler 구현체 클래스.
 */
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.error("Path {} occurred AccessDeniedException on CustomAccessDeniedHandler. -> ", request.getRequestURI(), accessDeniedException);
        ErrorResponse.sendErrorResponse(ErrorCodeType.FORBIDDEN_ONLY_PARTNER, request, response);
    }

}
