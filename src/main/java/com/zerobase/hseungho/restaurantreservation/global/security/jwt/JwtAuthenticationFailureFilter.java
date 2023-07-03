package com.zerobase.hseungho.restaurantreservation.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.hseungho.restaurantreservation.global.exception.base.BaseException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCode;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFailureFilter 는 요청이 Spring DispatchServlet 에 들어가기 이전에, <br>
 * JwtAuthenticationFilter 에서 발생하는 Exception 에 대해서 에러 모델을 반환하기 위한 필터 클래스.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFailureFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * JWT 토큰 파싱 및 검증에서 Exception 발생 시 해당 Exception 에 대한 에러 모델을 <br>
     * 반환하기 위한 메소드.
     * @param request Http 요청 객체
     * @param response Http 요청에 대한 응답 객체
     * @param filterChain Spring 의 FilterChain 인터페이스
     * @throws ServletException Servlet 처리 중 발생할 수 있음
     * @throws IOException IO 처리 중 발생할 수 있음
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BaseException e) {
            log.error("Path {} occurred Exception during authentication filter. -> ", request.getRequestURI(), e);
            sendErrorResponse(e.getErrorCodeType(), request, response);
        } catch (AccessDeniedException e) {
            log.error("Path {} occurred Access Denied Exception. -> ", request.getRequestURI(), e);
            sendErrorResponse(ErrorCodeType.FORBIDDEN, request, response);
        } catch (ExpiredJwtException e) {
            log.error("Path {} occurred Expired Token Exception. -> ", request.getRequestURI(), e);
            sendErrorResponse(ErrorCodeType.UNAUTHORIZED_TOKEN_EXPIRED, request, response);
        } catch (JwtException e) {
            log.error("Path {} occurred Token Exception. -> ", request.getRequestURI(), e);
            sendErrorResponse(ErrorCodeType.UNAUTHORIZED_TOKEN_INVALID, request, response);
        }
    }

    /**
     * 발생한 Exception 에 대한 ErrorCode 의 메시지를 응답 객체에 <br>
     * 담아 요청자에게 응답하는 메소드.
     * @param errorCode 발생한 ErrorCode
     * @param request Http 요청 객체
     * @param response Http 요청에 대한 응답 객체
     * @throws IOException IO 처리 중 발생할 수 있음
     */
    private void sendErrorResponse(ErrorCode errorCode,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                ErrorResponse.errorResponse(errorCode, request.getRequestURI())
        ));
    }

}
