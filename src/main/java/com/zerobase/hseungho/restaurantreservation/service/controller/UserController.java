package com.zerobase.hseungho.restaurantreservation.service.controller;

import com.zerobase.hseungho.restaurantreservation.service.appservice.UserService;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${service.api.prefix}")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("${service.api.user.check-id}")
    @ResponseStatus(HttpStatus.OK)
    public CheckUsingResourceAvailable.Response checkUserIdAvailable(@RequestParam("id") String userId) {
        return CheckUsingResourceAvailable.Response.of(
                userService.checkUserIdAvailable(userId)
        );
    }

    @GetMapping("${service.api.user.check-nickname}")
    @ResponseStatus(HttpStatus.OK)
    public CheckUsingResourceAvailable.Response checkNicknameAvailable(@RequestParam("nickname") String nickname) {
        return CheckUsingResourceAvailable.Response.of(
                userService.checkNicknameAvailable(nickname)
        );
    }

    @PostMapping("${service.api.user.sign-up}")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUp.Response signUp(@RequestBody @Valid SignUp.Request request) {
        return SignUp.Response.fromDto(
                userService.signUp(request)
        );
    }

    @PostMapping("${service.api.user.login}")
    @ResponseStatus(HttpStatus.OK)
    public Login.Response login(@RequestBody @Valid Login.Request request) {
        return Login.Response.fromTokenDto(
                userService.login(request)
        );
    }

    @GetMapping("${service.api.user.refresh}")
    @ResponseStatus(HttpStatus.OK)
    public TokenRefresh.Response refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {
        return TokenRefresh.Response.fromTokenDto(
                userService.refreshToken(refreshToken)
        );
    }

    @PatchMapping("${service.api.user.update-pw}")
    @ResponseStatus(HttpStatus.OK)
    public UpdatePassword.Response updatePassword(@RequestBody @Valid UpdatePassword.Request request) {
        return UpdatePassword.Response.fromDto(
                userService.updatePassword(request)
        );
    }

    @PostMapping("${service.api.user.register-partner}")
    @ResponseStatus(HttpStatus.OK)
    public RegisterPartner.Response registerPartner() {
        return RegisterPartner.Response.fromDto(
                userService.registerPartner()
        );
    }

}
