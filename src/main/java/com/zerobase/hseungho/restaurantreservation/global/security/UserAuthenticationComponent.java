package com.zerobase.hseungho.restaurantreservation.global.security;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.UnauthorizedException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * UserDetailsService 인터페이스를 구현체한 컴포넌트 클래스. <br>
 * 유저 PK ID로 DB 에서 유저를 조회하여 반환한다.
 */
@Component
@RequiredArgsConstructor
public class UserAuthenticationComponent implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException(ErrorCodeType.UNAUTHORIZED_TOKEN_INVALID));
    }
}
