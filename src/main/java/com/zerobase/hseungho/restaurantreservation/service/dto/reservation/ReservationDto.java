package com.zerobase.hseungho.restaurantreservation.service.dto.reservation;

import com.zerobase.hseungho.restaurantreservation.service.domain.reservation.Reservation;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.UserDto;
import com.zerobase.hseungho.restaurantreservation.service.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {

    private Long id;
    private String number;
    private Integer numOfPerson;
    private String clientContactNumber;
    private LocalDateTime reservedAt;
    private LocalDateTime canceledAt;
    private LocalDateTime visitedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime refusedAt;
    private LocalDateTime createdAt;
    private ReservationStatus status;
    private UserDto client;
    private RestaurantDto restaurant;

    public static ReservationDto empty() {
        return ReservationDto.builder()
                .client(UserDto.empty())
                .restaurant(RestaurantDto.empty())
                .build();
    }

    public static ReservationDto fromEntity(Reservation entity) {
        return ReservationDto.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .numOfPerson(entity.getNumOfPerson())
                .clientContactNumber(entity.getClientContactNumber())
                .reservedAt(entity.getReservedAt())
                .canceledAt(entity.getCanceledAt())
                .visitedAt(entity.getVisitedAt())
                .approvedAt(entity.getApprovedAt())
                .refusedAt(entity.getRefusedAt())
                .createdAt(entity.getCreatedAt())
                .status(entity.getStatus())
                .client(UserDto.fromEntity(entity.getClient()))
                .restaurant(RestaurantDto.fromEntity(entity.getRestaurant()))
                .build();
    }

}
