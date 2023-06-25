package com.zerobase.hseungho.restaurantreservation.service.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.hseungho.restaurantreservation.global.config.SecurityConfiguration;
import com.zerobase.hseungho.restaurantreservation.global.security.jwt.JwtAuthenticationFilter;
import com.zerobase.hseungho.restaurantreservation.service.appservice.UserServiceImpl;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Value("${service.basic-api}")
    private String BASIC_API;
    
    @Test
    @DisplayName("UserController - 아이디 중복확인 성공 (true)")
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
    @DisplayName("UserController - 아이디 중복확인 성공 (false)")
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
    @DisplayName("UserController - 닉네임 중복확인 성공 (true)")
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
    @DisplayName("UserController - 닉네임 중복확인 성공 (false)")
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

}