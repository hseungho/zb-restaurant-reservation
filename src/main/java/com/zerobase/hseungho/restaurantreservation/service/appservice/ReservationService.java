package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReservationDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.reservation.ReserveReservation;

public interface ReservationService {

    /**
     *
     * @param restaurantId
     * @param request
     * @return
     */
    ReservationDto reserve(Long restaurantId, ReserveReservation.Request request);

}
