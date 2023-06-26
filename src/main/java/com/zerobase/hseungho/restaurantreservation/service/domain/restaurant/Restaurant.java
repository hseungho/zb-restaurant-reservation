package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.domain.base.BaseAuditingEntity;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "restaurant")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Restaurant extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private AddressVO addressInfo;
    private String description;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private Integer countOfTables;
    private Integer maxPerReservation;
    private String contactNumber;
    private Double rating;
    private LocalDateTime deleteReqAt;
    private LocalDateTime deletedAt;
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Menu> menus;
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Review> reviews;
    @OneToOne
    private User manager;


}
