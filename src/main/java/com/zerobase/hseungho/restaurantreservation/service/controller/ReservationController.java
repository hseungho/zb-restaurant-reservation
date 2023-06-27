package com.zerobase.hseungho.restaurantreservation.service.controller;

import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.FindImpossibleReservation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("${service.api.prefix}")
@RequiredArgsConstructor
public class ReservationController {

    @GetMapping("${service.api.reservation.find-impossible-reservation}")
    @ResponseStatus(HttpStatus.OK)
    public FindImpossibleReservation.Response findImpossibleReservation(@PathVariable("restaurantId") Long restaurantId,
                                                                        @RequestParam("reqDate") LocalDate reqDate) {
        return FindImpossibleReservation.Response.fromDto();
    }

}
