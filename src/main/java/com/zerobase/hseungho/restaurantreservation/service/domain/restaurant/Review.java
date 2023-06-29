package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.domain.base.BaseAuditingEntity;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User author;
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false, updatable = false)
    private Restaurant restaurant;

    public static Review create(Double rating, String content, String imageSrc, User author) {
        return Review.builder()
                .rating(rating)
                .content(content)
                .imageSrc(imageSrc)
                .author(author)
                .build();
    }

    public void associate(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

}
