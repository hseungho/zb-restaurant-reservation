package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.domain.base.BaseAuditingEntity;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private AddressVO addressVO;
    private String description;
    @Embedded
    private RestaurantTimeVO restaurantTimeVO;
    private Integer countOfTables;
    private Integer maxPerReservation;
    private String contactNumber;
    private Double rating;
    private LocalDateTime deleteReqAt;
    private LocalDateTime deletedAt;
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Menu> menus = new ArrayList<>();
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();
    @OneToOne
    private User manager;

    public static Restaurant create(SaveRestaurant.Request request) {
        Restaurant restaurant = new Restaurant();
        restaurant.name = request.getName();
        restaurant.addressVO = new AddressVO(request.getAddress(), request.getX(), request.getY());
        restaurant.description = request.getDescription();
        restaurant.restaurantTimeVO = new RestaurantTimeVO(
                request.getOpenTime().getHour(), request.getOpenTime().getMinute(),
                request.getCloseTime().getHour(), request.getCloseTime().getMinute()
        );
        restaurant.countOfTables = request.getCountOfTables();
        restaurant.maxPerReservation = request.getMaxPerReservation();
        restaurant.contactNumber = request.getContactNumber();
        return restaurant;
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
        menu.associate(this);
    }


}
