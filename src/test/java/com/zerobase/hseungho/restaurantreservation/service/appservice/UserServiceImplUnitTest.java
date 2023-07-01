package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.security.jwt.JwtComponent;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.TokenDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.UpdatePassword;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.UpdateProfile;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.UserDto;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import com.zerobase.hseungho.restaurantreservation.util.MockBuilder;
import com.zerobase.hseungho.restaurantreservation.util.TestSecurityHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtComponent jwtComponent;

    @Test
    @DisplayName("액세스 토큰 리프레시")
    void test_refreshToken() {
        // given
        User user = MockBuilder.mockUser(UserType.ROLE_CUSTOMER);
        given(jwtComponent.separatePrefix(anyString()))
                .willReturn("prev refreshToken");
        given(jwtComponent.getAuthentication(anyString()))
                .willReturn(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
        given(jwtComponent.generateAccessToken(anyString(), any()))
                .willReturn("new accessToken");
        given(jwtComponent.generateRefreshToken(anyString(), any()))
                .willReturn("new refreshToken");
        // when
        TokenDto result = userService.refreshToken("prev refreshToken");
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("new accessToken", result.getAccessToken());
        Assertions.assertEquals("new refreshToken", result.getRefreshToken());
    }

    @Test
    @DisplayName("비밀번호 변경")
    void test_updatePassword() {
        // given
        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_CUSTOMER);
        given(userRepository.findById(anyString()))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString()))
                .willReturn(true);
        given(passwordEncoder.encode(anyString()))
                .willReturn("encodedpassword");
        // when
        UserDto result = userService.updatePassword(UpdatePassword.Request.builder()
                .currentPassword("currentpassword")
                .newPassword("newpassword1234!")
                .build());
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getUserId(), result.getUserId());
        Assertions.assertEquals(user.getNickname(), result.getNickname());
        Assertions.assertEquals(user.getType(), result.getType());
    }

    @Test
    @DisplayName("내 정보 조회")
    void test_findProfile() {
        // given
        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_CUSTOMER);
        // when
        UserDto result = userService.findProfile();
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getUserId(), result.getUserId());
        Assertions.assertEquals(user.getNickname(), result.getNickname());
        Assertions.assertEquals(user.getType(), result.getType());
    }

    @Test
    @DisplayName("내 정보 수정")
    void test_updateProfile() {
        // given
        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_CUSTOMER);
        given(userRepository.findById(anyString()))
                .willReturn(Optional.of(user));
        given(userRepository.existsByNickname(anyString()))
                .willReturn(false);
        // when
        UserDto result = userService.updateProfile(UpdateProfile.Request.builder()
                .nickname("newNickname").build());
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getUserId(), result.getUserId());
        Assertions.assertEquals(user.getType(), result.getType());
        Assertions.assertEquals("newNickname", result.getNickname());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void test_resign() {
        // given
        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_PARTNER);
        given(userRepository.findById(anyString()))
                .willReturn(Optional.of(user));
        given(restaurantRepository.findByManager(any()))
                .willReturn(Optional.of(MockBuilder.mockDeletedRestaurant(user)));
        // when
        UserDto result = userService.resign();
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getUserId(), result.getUserId());
        Assertions.assertEquals(user.getNickname(), result.getNickname());
        Assertions.assertEquals(user.getType(), result.getType());
    }
}
