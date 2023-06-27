package com.zerobase.hseungho.restaurantreservation.service.domain.reservation;

import com.zerobase.hseungho.restaurantreservation.service.domain.base.BaseAuditingEntity;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.type.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity(name = "reservation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Reservation extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;
    @Column(name = "number", nullable = false, unique = true, updatable = false)
    private String number;
    @Column(name = "num_of_person", nullable = false)
    private Integer numOfPerson;
    @Column(name = "client_contact_number", nullable = false)
    private String clientContactNumber;
    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;
    @Column(name = "visited_at")
    private LocalDateTime visitedAt;
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    @Column(name = "refused_at")
    private LocalDateTime refusedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

}