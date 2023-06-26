package com.zerobase.hseungho.restaurantreservation.global.security.jwt;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.UnauthorizedException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.security.SecurityHolder;
import com.zerobase.hseungho.restaurantreservation.service.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.zerobase.hseungho.restaurantreservation.global.constants.TokenConstants.TOKEN_HEADER;
import static com.zerobase.hseungho.restaurantreservation.global.constants.TokenConstants.TOKEN_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtComponent jwtComponent;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = this.resolveTokenFromRequest(request);

        if (StringUtils.hasText(token) && jwtComponent.validateToken(token)) {
            Authentication authentication = this.jwtComponent.getAuthentication(token);

            if (((User) authentication.getPrincipal()).isResigned()) {
                throw new UnauthorizedException(ErrorCodeType.UNAUTHORIZED_LOGIN_ALREADY_RESIGNED_USER);
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Holder -> {}", SecurityHolder.getUser());
        }

        filterChain.doFilter(request, response);
    }

    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
