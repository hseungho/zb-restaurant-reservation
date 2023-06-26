package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.UserDto;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplPartnerUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("파트너 등록 성공")
    @WithMockUser(value = "ROLE_CUSTOMER")
    void test_registerPartner_success() {
        // given
        User user = User.builder()
                .userId("testid")
                .password("testpassword1234!")
                .nickname("testnickname")
                .type(UserType.ROLE_CUSTOMER)
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities())
        );
        // when
        UserDto userDto = userService.registerPartner();
        // then
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(UserType.ROLE_PARTNER, userDto.getType());
    }

    @Test
    @DisplayName("파트너 등록 실패 - 이미 파트너 유저")
    void test_registerPartner_failure_cause_already_partner() {
        // given
        User user = User.builder()
                .userId("testid")
                .password("testpassword1234!")
                .nickname("testnickname")
                .type(UserType.ROLE_PARTNER)
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities())
        );
        // when
        BadRequestException ex = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.registerPartner()
        );
        // then
        Assertions.assertEquals(ErrorCodeType.BAD_REQUEST_PARTNER_ALREADY, ex.getErrorCodeType());
    }

}
