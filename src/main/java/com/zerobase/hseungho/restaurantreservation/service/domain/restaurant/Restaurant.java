package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.domain.base.BaseAuditingEntity;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SaveRestaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "restaurant")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Restaurant extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Embedded
    private AddressVO addressVO;
    @Column(name = "description")
    private String description;
    @Embedded
    private RestaurantTimeVO restaurantTimeVO;
    @Column(name = "count_of_tables", nullable = false)
    private Integer countOfTables;
    @Column(name = "max_per_reservation")
    private Integer maxPerReservation;
    @Column(name = "contact_number", nullable = false)
    private String contactNumber;
    @Column(name = "rating")
    private Double rating;
    @Column(name = "delete_req_at")
    private LocalDateTime deleteReqAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @OneToMany(
            mappedBy = "restaurant",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Menu> menus = new ArrayList<>();
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    public static Restaurant create(SaveRestaurant.Request request, User manager) {
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
        restaurant.manager = manager;
        return restaurant;
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
        menu.associate(this);
    }

    public boolean isManager(User user) {
        return Objects.equals(manager.getId(), user.getId());
    }

    public boolean isValidReserveAt(LocalDateTime requestTime) {
        return this.restaurantTimeVO.isContainsRestaurantTimes(
                requestTime.getHour(),
                requestTime.getMinute()
        );
    }

    public boolean isAfterDeleteReqAt(LocalDateTime time) {
        if (deleteReqAt == null) return false;

        return time.isAfter(this.deleteReqAt);
    }

}
