package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.domain.base.BaseAuditingEntity;
import com.zerobase.hseungho.restaurantreservation.service.domain.reservation.Reservation;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Entity(name = "review")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Review extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;
    @Column(name = "rating", nullable = false)
    private Double rating;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "image_src")
    private String imageSrc;
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    private User author;
    @ManyToOne
    @JoinColumn(name = "restaurant_id", updatable = false)
    private Restaurant restaurant;
    @ManyToOne
    @JoinColumn(name = "reservation_id", updatable = false)
    private Reservation reservation;


    public static Review create(Double rating, String content, String imageSrc) {
        return Review.builder()
                .rating(rating)
                .content(content)
                .imageSrc(imageSrc)
                .build();
    }

    public void update(Double rating, String content, String imageSrc) {
        this.rating = rating;
        this.content = content;
        this.imageSrc = imageSrc;
    }

    public void associate(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void associate(User user) {
        this.author = user;
    }

    public void associate(Reservation reservation) {
        this.reservation = reservation;
    }

    public boolean isAuthorId(String aId) {
        return Objects.equals(this.author.getId(), aId);
    }

    public void dissociate() {
        this.author = null;
        this.restaurant = null;
    }

}
