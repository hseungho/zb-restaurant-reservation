package com.zerobase.hseungho.restaurantreservation.service.controller;

import com.zerobase.hseungho.restaurantreservation.service.appservice.ReservationService;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ApproveReservation;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.CancelReservation;
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
    public ReserveReservation.Response reserve(@RequestBody @Valid ReserveReservation.Request request) {
        return ReserveReservation.Response.fromDto(
                reservationService.reserve(request)
        );
    }

    @PostMapping("${service.api.reservation.cancel}")
    @ResponseStatus(HttpStatus.OK)
    public CancelReservation.Response cancel(@PathVariable("reservationId") Long reservationId) {
        return CancelReservation.Response.fromDto(
                reservationService.cancel(reservationId)
        );
    }

    @PostMapping("${service.api.reservation.approve}")
    @ResponseStatus(HttpStatus.OK)
    public ApproveReservation.Response approve(@PathVariable("reservationId") Long reservationId) {
        return ApproveReservation.Response.fromDto(
                reservationService.approve(reservationId)
        );
    }

}
