package com.zerobase.hseungho.restaurantreservation.service.controller.user;

import com.zerobase.hseungho.restaurantreservation.service.appservice.UserService;
import com.zerobase.hseungho.restaurantreservation.service.dto.external.user.CheckIdAvailable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${service.basic-api}")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/sign-up/check-id")
    @ResponseStatus(HttpStatus.OK)
    public CheckIdAvailable.Response checkIdAvailable(@RequestParam("id") String userId) {
        return CheckIdAvailable.Response.of(
                userService.checkIdAvailable(userId)
        );
    }

}
