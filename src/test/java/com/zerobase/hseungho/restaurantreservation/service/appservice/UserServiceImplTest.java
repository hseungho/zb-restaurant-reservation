package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.domain.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.external.user.SignUp;
import com.zerobase.hseungho.restaurantreservation.service.dto.internal.user.UserDto;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
    @DisplayName("UserService - 닉네임 중복확인 사용불가")
    void test_checkNicknameAvailable_failure() {
        // given
        given(userRepository.existsByNickname(anyString()))
                .willReturn(true);
        // when
        boolean result = userService.checkNicknameAvailable("test_nickname");
        // then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("UserService - 회원가입 성공")
    void test_signUp_success() {
        // given
        final String USER_ID = "mock_user_id";
        final String NICKNAME = "mock_nickname";
        given(userRepository.existsByUserId(anyString()))
                .willReturn(false);
        given(userRepository.existsByNickname(anyString()))
                .willReturn(false);
        given(userRepository.save(any()))
                .willReturn(User.builder()
                        .userId(USER_ID)
                        .password("encrypt_password")
                        .nickname(NICKNAME)
                        .type(UserType.ROLE_CUSTOMER)
                        .build()
                );
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        // when
        UserDto userDto = userService.signUp(SignUp.Request.builder()
                .userId("").password("").nickname("")
                .build());
        // then
        verify(userRepository, times(1)).save(captor.capture());
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(USER_ID, userDto.getUserId());
        Assertions.assertEquals(NICKNAME, userDto.getNickname());
        Assertions.assertEquals(UserType.ROLE_CUSTOMER, userDto.getType());
    }


}