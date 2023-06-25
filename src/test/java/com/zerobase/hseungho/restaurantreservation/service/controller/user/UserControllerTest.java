package com.zerobase.hseungho.restaurantreservation.service.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.hseungho.restaurantreservation.global.config.SecurityConfiguration;
import com.zerobase.hseungho.restaurantreservation.global.security.jwt.JwtAuthenticationFilter;
import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import com.zerobase.hseungho.restaurantreservation.service.appservice.UserServiceImpl;
import com.zerobase.hseungho.restaurantreservation.service.controller.UserController;
import com.zerobase.hseungho.restaurantreservation.service.dto.external.user.SignUp;
import com.zerobase.hseungho.restaurantreservation.service.dto.internal.UserDto;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
        }
)
@Import(SecurityConfiguration.class)
class UserControllerTest {

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${service.api.prefix}")
    private String BASIC_API;
    
    @Test
    @DisplayName("아이디 중복확인 성공 (true)")
    void test_checkUserIdAvailable_success_true() throws Exception {
        // given
        final String url = BASIC_API + "/sign-up/check-id?id=mocktest12341234";
        given(userService.checkUserIdAvailable(anyString()))
                .willReturn(true);
        // when
        // then
        mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }

    @Test
    @DisplayName("아이디 중복확인 성공 (false)")
    void test_checkUserIdAvailable_success_false() throws Exception {
        // given
        final String url = BASIC_API + "/sign-up/check-id?id=mocktest12341234";
        given(userService.checkUserIdAvailable(anyString()))
                .willReturn(false);
        // when
        // then
        mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(false));
    }

    @Test
    @DisplayName("닉네임 중복확인 성공 (true)")
    void test_checkNicknameAvailable_success_true() throws Exception {
        // given
        final String url = BASIC_API + "/sign-up/check-nickname?nickname=테스트닉네임입니다";
        given(userService.checkNicknameAvailable(anyString()))
                .willReturn(true);
        // when
        // then
        mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }

    @Test
    @DisplayName("닉네임 중복확인 성공 (false)")
    void test_checkNicknameAvailable_success_false() throws Exception {
        // given
        final String url = BASIC_API + "/sign-up/check-nickname?nickname=테스트닉네임입니다";
        given(userService.checkNicknameAvailable(anyString()))
                .willReturn(false);
        // when
        // then
        mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(false));
    }

    @Test
    @DisplayName("회원가입 성공")
    void test_signUp_success() throws Exception {
        // given
        final String url = BASIC_API + "/sign-up";
        LocalDateTime now = SeoulDateTime.now();
        given(userService.signUp(any()))
                .willReturn(UserDto.builder()
                                .id("id")
                                .userId("user_id")
                                .nickname("nickname")
                                .type(UserType.ROLE_CUSTOMER)
                                .createdAt(now)
                                .build());
        // when
        // then
        mockMvc.perform(post(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        SignUp.Request.builder()
                                .userId("user_id")
                                .password("password1234!")
                                .nickname("nickname")
                                .build()
                )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user_id"))
                .andExpect(jsonPath("$.nickname").value("nickname"))
                .andExpect(jsonPath("$.userType").value(UserType.ROLE_CUSTOMER.name()))
                .andExpect(jsonPath("$.createdAt").value(now.toString()));
    }

}