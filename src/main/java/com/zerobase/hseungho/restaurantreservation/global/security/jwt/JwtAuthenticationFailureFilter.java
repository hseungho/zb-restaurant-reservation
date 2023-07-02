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

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFailureFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BaseException e) {
            log.error("Occurred Exception during authentication filter. -> ", e);
            sendErrorResponse(e.getErrorCodeType(), request, response);
        } catch (AccessDeniedException e) {
            log.error("Occurred Access Denied Exception. -> ", e);
            sendErrorResponse(ErrorCodeType.FORBIDDEN, request, response);
        } catch (ExpiredJwtException e) {
            log.error("Occurred Expired Token Exception. -> ", e);
            sendErrorResponse(ErrorCodeType.UNAUTHORIZED_TOKEN_EXPIRED, request, response);
        } catch (JwtException e) {
            log.error("Occurred Token Exception. -> ", e);
            sendErrorResponse(ErrorCodeType.UNAUTHORIZED_TOKEN_INVALID, request, response);
        }
    }

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
