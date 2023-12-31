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
    public CheckUsingResourceAvailable.Response checkUserIdAvailable(@RequestParam("key") String userId) {
        return CheckUsingResourceAvailable.Response.of(
                userService.checkUserIdAvailable(userId)
        );
    }

    @GetMapping("${service.api.user.check-nickname}")
    @ResponseStatus(HttpStatus.OK)
    public CheckUsingResourceAvailable.Response checkNicknameAvailable(@RequestParam("key") String nickname) {
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

    @GetMapping("${service.api.user.find-profile}")
    @ResponseStatus(HttpStatus.OK)
    public FindProfile.Response findProfile() {
        return FindProfile.Response.fromDto(
                userService.findProfile()
        );
    }

    @PatchMapping("${service.api.user.update-profile}")
    @ResponseStatus(HttpStatus.OK)
    public UpdateProfile.Response updateProfile(@RequestBody @Valid UpdateProfile.Request request) {
        return UpdateProfile.Response.fromDto(
                userService.updateProfile(request)
        );
    }

    @DeleteMapping("${service.api.user.resign}")
    @ResponseStatus(HttpStatus.OK)
    public Resign.Response resign() {
        return Resign.Response.fromDto(
                userService.resign()
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
