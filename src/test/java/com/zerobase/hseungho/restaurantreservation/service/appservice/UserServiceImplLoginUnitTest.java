package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.security.jwt.JwtComponent;
import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.Login;
import com.zerobase.hseungho.restaurantreservation.service.dto.TokenDto;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplLoginUnitTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtComponent jwtComponent;

    @Test
    @DisplayName("로그인 성공")
    void test_login_success() {
        // given
        final LocalDateTime now = SeoulDateTime.now();
        given(userRepository.findByUserId(anyString()))
                .willReturn(Optional.of(
                        User.builder()
                                .userId("user-id")
                                .password("encodedPw")
                                .nickname("nickname")
                                .type(UserType.ROLE_CUSTOMER)
                                .createdAt(now.minusDays(1))
                                .loggedInAt(now)
                                .build()
                ));
        given(passwordEncoder.matches(any(), any()))
                .willReturn(true);
        given(jwtComponent.generateAccessToken(anyString(), any()))
                .willReturn("Bearer accessToken");
        given(jwtComponent.generateRefreshToken(anyString(), any()))
                .willReturn("Bearer refreshToken");
        // when
        TokenDto tokenDto = userService.login(
                Login.Request.builder()
                        .userId("user-id")
                        .password("password1234!")
                        .build());
        // then
        Assertions.assertFalse(tokenDto.getAccessToken().isBlank());
        Assertions.assertFalse(tokenDto.getRefreshToken().isBlank());
        Assertions.assertNotNull(tokenDto.getLoggedInAt());
        Assertions.assertTrue(tokenDto.getAccessToken().contains("Bearer "));
        Assertions.assertTrue(tokenDto.getRefreshToken().contains("Bearer "));
    }

}
