package com.zerobase.hseungho.restaurantreservation.service.domain.reservation;

import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import com.zerobase.hseungho.restaurantreservation.service.domain.base.BaseAuditingEntity;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Review;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.type.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<Review> reviews;

    public static Reservation create(String number,
                                     int numOfPerson,
                                     String clientContactNumber,
                                     LocalDateTime reservedAt,
                                     User client,
                                     Restaurant restaurant) {
        Reservation reservation = Reservation.builder()
                .number(number)
                .numOfPerson(numOfPerson)
                .clientContactNumber(clientContactNumber)
                .reservedAt(reservedAt)
                .status(ReservationStatus.RESERVED)
                .build();
        reservation.associate(client);
        reservation.associate(restaurant);
        return reservation;
    }

    public void associate(User client) {
        this.client = client;
    }

    public void associate(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public boolean isClient(User client) {
        return Objects.equals(this.client.getId(), client.getId());
    }

    public boolean isDeletedRestaurant() {
        return this.restaurant == null || this.restaurant.isDeleted();
    }

    public boolean isCanceled() {
        return this.status == ReservationStatus.CANCELED;
    }

    public void cancel() {
        this.canceledAt = SeoulDateTime.now();
        this.status = ReservationStatus.CANCELED;
    }

    public boolean isApproved() {
        return this.status == ReservationStatus.APPROVED;
    }

    public boolean isReserved() {
        return this.status == ReservationStatus.RESERVED;
    }

    public void approve() {
        this.approvedAt = SeoulDateTime.now();
        this.status = ReservationStatus.APPROVED;
    }

    public boolean isRefused() {
        return this.status == ReservationStatus.REFUSED;
    }

    public void refuse() {
        this.refusedAt = SeoulDateTime.now();
        this.status = ReservationStatus.REFUSED;
    }

    public boolean isVisited() {
        return this.status == ReservationStatus.VISITED;
    }

    public void visit() {
        this.visitedAt = SeoulDateTime.now();
        this.status = ReservationStatus.VISITED;
    }

    public boolean isAlreadyReviewer(User user) {
        return this.reviews.stream()
                .anyMatch(it -> it.isAuthorId(user.getId()));
    }

    public void addReview(Review review) {
        this.reviews.add(review);
        review.associate(this);
    }
}
