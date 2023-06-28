package com.zerobase.hseungho.restaurantreservation.service.controller;

import com.zerobase.hseungho.restaurantreservation.service.appservice.ReservationService;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReserveReservation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${service.api.prefix}")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("${service.api.reservation.reserve}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReserveReservation.Response reserve(@PathVariable("restaurantId") Long restaurantId,
                                               @RequestBody @Valid ReserveReservation.Request request) {
        return ReserveReservation.Response.fromDto(
                reservationService.reserve(restaurantId, request)
        );
    }

}
