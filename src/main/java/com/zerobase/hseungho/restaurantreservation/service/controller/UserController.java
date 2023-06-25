package com.zerobase.hseungho.restaurantreservation.service.controller;

import com.zerobase.hseungho.restaurantreservation.service.appservice.UserService;
import com.zerobase.hseungho.restaurantreservation.service.dto.external.user.CheckUsingResourceAvailable;
import com.zerobase.hseungho.restaurantreservation.service.dto.external.user.SignUp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

}
