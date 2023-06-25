package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("UserService - 아이디 중복확인 사용가능")
    void test_checkIdAvailable_success() {
        // given
        given(userRepository.existsByUserId(anyString()))
                .willReturn(false);
        // when
        boolean result = userService.checkUserIdAvailable("test_id");
        // then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("UserService - 아이디 중복확인 사용불가")
    void test_checkIdAvailable_failure() {
        // given
        given(userRepository.existsByUserId(anyString()))
                .willReturn(true);
        // when
        boolean result = userService.checkUserIdAvailable("test_id");
        // then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("userService - 닉네임 중복확인 사용가능")
    void test_checkNicknameAvailable_success() {
        // given
        given(userRepository.existsByNickname(anyString()))
                .willReturn(false);
        // when
        boolean result = userService.checkNicknameAvailable("test_nickname");
        // then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("userService - 닉네임 중복확인 사용불가")
    void test_checkNicknameAvailable_failure() {
        // given
        given(userRepository.existsByNickname(anyString()))
                .willReturn(true);
        // when
        boolean result = userService.checkNicknameAvailable("test_nickname");
        // then
        Assertions.assertFalse(result);
    }


}