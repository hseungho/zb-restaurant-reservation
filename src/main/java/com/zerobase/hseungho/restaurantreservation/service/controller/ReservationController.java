package com.zerobase.hseungho.restaurantreservation.service.controller;

import com.zerobase.hseungho.restaurantreservation.service.appservice.ReservationService;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @PostMapping("${service.api.reservation.refuse}")
    @ResponseStatus(HttpStatus.OK)
    public RefuseReservation.Response refuse(@PathVariable("reservationId") Long reservationId) {
        return RefuseReservation.Response.fromDto(
                reservationService.refuse(reservationId)
        );
    }

    @PostMapping("${service.api.reservation.visit}")
    @ResponseStatus(HttpStatus.OK)
    public VisitReservation.Response visit(@PathVariable("reservationId") Long reservationId) {
        return VisitReservation.Response.fromDto(
                reservationService.visit(reservationId)
        );
    }

    @GetMapping("${service.api.reservation.find-list-client}")
    @ResponseStatus(HttpStatus.OK)
    public FindReservationList.Response findClientReservations(@RequestParam(value = "date", required = false) LocalDate date,
                                                               @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return FindReservationList.Response.fromDto(
                reservationService.findClientReservations(date, pageable)
        );
    }

    @GetMapping("${service.api.reservation.find-list-manager}")
    @ResponseStatus(HttpStatus.OK)
    public FindReservationList.Response findManagerReservations(@RequestParam(value = "date", required = false) LocalDate date,
                                                               @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return FindReservationList.Response.fromDto(
                reservationService.findManagerReservations(date, pageable)
        );
    }

}
