package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
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
    @DisplayName("아이디 중복확인 사용가능")
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
    @DisplayName("아이디 중복확인 사용불가")
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
    @DisplayName("닉네임 중복확인 사용가능")
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
    @DisplayName("닉네임 중복확인 사용불가")
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
    @DisplayName("회원가입 성공")
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
        UserDto userDto = userService.signUp(
                signUpRequestBuilder(true, true, true)
                        .build());
        // then
        verify(userRepository, times(1)).save(captor.capture());
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(USER_ID, userDto.getUserId());
        Assertions.assertEquals(NICKNAME, userDto.getNickname());
        Assertions.assertEquals(UserType.ROLE_CUSTOMER, userDto.getType());
    }

    @Test
    @DisplayName("회원가입 실패 - 유저 아이디 Blank")
    void test_signUp_failure_cause_userIdBlank() {
        // given
        // when
        // then
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.signUp(
                        signUpRequestBuilder(false, true, true)
                                .build()
                ));
        Assertions.assertEquals(ErrorCodeType.BAD_REQUEST_SIGN_UP_BLANK, exception.getErrorCodeType());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 Blank")
    void test_signUp_failure_cause_passwordBlank() {
        // given
        // when
        // then
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.signUp(
                        signUpRequestBuilder(true, false, true)
                                .build()
                ));
        Assertions.assertEquals(ErrorCodeType.BAD_REQUEST_SIGN_UP_BLANK, exception.getErrorCodeType());
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 Blank")
    void test_signUp_failure_cause_nicknameBlank() {
        // given
        // when
        // then
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.signUp(
                        signUpRequestBuilder(false, true, true)
                                .build()
                ));
        Assertions.assertEquals(ErrorCodeType.BAD_REQUEST_SIGN_UP_BLANK, exception.getErrorCodeType());
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void test_signUp_failure_cause_idDuplication() {
        // given
        given(userRepository.existsByUserId(anyString()))
                .willReturn(true);
        // when
        // then
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.signUp(
                        signUpRequestBuilder(true, true, true)
                                .build()
                ));
        Assertions.assertEquals(ErrorCodeType.BAD_REQUEST_SIGN_UP_USER_ID_DUPLICATED, exception.getErrorCodeType());
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    void test_signUp_failure_cause_nicknameDuplication() {
        // given
        given(userRepository.existsByUserId(anyString()))
                .willReturn(false);
        given(userRepository.existsByNickname(anyString()))
                .willReturn(true);
        // when
        // then
        BadRequestException ex = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.signUp(
                        signUpRequestBuilder(true, true, true)
                                .build()
                ));
        Assertions.assertEquals(ErrorCodeType.BAD_REQUEST_SIGN_UP_NICKNAME_DUPLICATED, ex.getErrorCodeType());
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 길이")
    void test_signUp_failure_cause_nicknameLength() {
        // given
        given(userRepository.existsByUserId(anyString()))
                .willReturn(false);
        given(userRepository.existsByNickname(anyString()))
                .willReturn(false);
        // when
        // then
        BadRequestException ex = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.signUp(
                        signUpRequestBuilder(true, true, false)
                                .nickname("qwetewladfjlkfjsaffjasldfkj")
                                .build()
                ));
        Assertions.assertEquals(ErrorCodeType.BAD_REQUEST_SIGN_UP_NICKNAME_LENGTH, ex.getErrorCodeType());
    }
    
    @Test
    @DisplayName("회원가입 실패 - 비밀번호 정규식 실패 - 8자리 이하")
    void test_signUp_failure_cause_passwordInvalidRegex_1() {
        // given
        given(userRepository.existsByUserId(anyString()))
                .willReturn(false);
        given(userRepository.existsByNickname(anyString()))
                .willReturn(false);
        // when
        // then
        BadRequestException ex = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.signUp(
                        signUpRequestBuilder(true, false, true)
                                .password("test12!")
                                .build()
                ));
        Assertions.assertEquals(ErrorCodeType.BAD_REQUEST_SIGN_UP_PASSWORD, ex.getErrorCodeType());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 정규식 실패 - 특수문자 없음")
    void test_signUp_failure_cause_passwordInvalidRegex_2() {
        // given
        given(userRepository.existsByUserId(anyString()))
                .willReturn(false);
        given(userRepository.existsByNickname(anyString()))
                .willReturn(false);
        // when
        // then
        BadRequestException ex = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.signUp(
                        signUpRequestBuilder(true, false, true)
                                .password("test123412")
                                .build()
                ));
        Assertions.assertEquals(ErrorCodeType.BAD_REQUEST_SIGN_UP_PASSWORD, ex.getErrorCodeType());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 정규식 실패 - 공백 포함")
    void test_signUp_failure_cause_passwordInvalidRegex_3() {
        // given
        given(userRepository.existsByUserId(anyString()))
                .willReturn(false);
        given(userRepository.existsByNickname(anyString()))
                .willReturn(false);
        // when
        // then
        BadRequestException ex = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.signUp(
                        signUpRequestBuilder(true, false, true)
                                .password("test  1234 !")
                                .build()
                ));
        Assertions.assertEquals(ErrorCodeType.BAD_REQUEST_SIGN_UP_PASSWORD, ex.getErrorCodeType());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 정규식 실패 - 아이디 포함")
    void test_signUp_failure_cause_passwordInvalidRegex_4() {
        // given
        given(userRepository.existsByUserId(anyString()))
                .willReturn(false);
        given(userRepository.existsByNickname(anyString()))
                .willReturn(false);
        // when
        // then
        BadRequestException ex = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.signUp(
                        signUpRequestBuilder(true, false, true)
                                .password("test_id1234!")
                                .build()
                ));
        Assertions.assertEquals(ErrorCodeType.BAD_REQUEST_SIGN_UP_PASSWORD, ex.getErrorCodeType());
    }

    private SignUp.Request.RequestBuilder signUpRequestBuilder(boolean existUserId,
                                         boolean existPassword,
                                         boolean existNickname) {
        SignUp.Request.RequestBuilder builder = SignUp.Request.builder();
        if (existUserId) builder.userId("test_id");
        if (existPassword) builder.password("tester1234!");
        if (existNickname) builder.nickname("test_nick");
        return builder;
    }

}