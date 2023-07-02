package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.domain.base.BaseAuditingEntity;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SaveRestaurant;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.UpdateRestaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @Column(name = "open", nullable = false)
    private LocalTime open;
    @Column(name = "close", nullable = false)
    private LocalTime close;
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
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private List<Menu> menus = new ArrayList<>();
    @OneToMany(
            mappedBy = "restaurant",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private List<Review> reviews = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "user_id")
    private User manager;

    public static Restaurant create(SaveRestaurant.Request request, User manager) {
        Restaurant restaurant = new Restaurant();
        restaurant.name = request.getName();
        restaurant.addressVO = new AddressVO(request.getAddress(), request.getX(), request.getY());
        restaurant.description = request.getDescription();
        restaurant.open = LocalTime.of(request.getOpenTime().getHour(), request.getOpenTime().getMinute());
        restaurant.close = LocalTime.of(request.getCloseTime().getHour(), request.getCloseTime().getMinute());
        restaurant.countOfTables = request.getCountOfTables();
        restaurant.maxPerReservation = request.getMaxPerReservation();
        restaurant.contactNumber = request.getContactNumber();
        restaurant.rating = 0.0;
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
        LocalTime reqAt = LocalTime.of(requestTime.getHour(), requestTime.getMinute());
        return this.open.isBefore(reqAt) && this.close.isAfter(reqAt);
    }

    public boolean isAfterDeleteReqAt(LocalDateTime time) {
        if (deleteReqAt == null) return false;
        return time.isAfter(this.deleteReqAt);
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.associate(this);

        rating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average().orElse(1.0);
    }

    public void update(UpdateRestaurant.Request request) {
        this.name = request.getName();
        this.addressVO = new AddressVO(request.getAddress(), request.getX(), request.getY());
        this.description = request.getDescription();
        this.open = LocalTime.of(request.getOpenTime().getHour(), request.getOpenTime().getMinute());
        this.close = LocalTime.of(request.getCloseTime().getHour(), request.getCloseTime().getMinute());
        this.countOfTables = request.getCountOfTables();
        this.maxPerReservation = request.getMaxPerReservation();
        this.contactNumber = request.getContactNumber();
    }

    public void deleteNow(LocalDateTime now) {
        this.deleteReqAt = now;
        this.delete(now);
    }

    public void delete(LocalDateTime now) {
        this.deletedAt = now;
        this.manager = null;
        this.menus.forEach(Menu::dissociate);
        this.menus.clear();
        this.reviews.forEach(Review::dissociate);
        this.reviews.clear();
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
        review.dissociate();
    }

    public void requestDeleting(LocalDate date) {
        this.deleteReqAt = date.atStartOfDay();
    }

    public void removeMenu(Menu menu) {
        this.menus.remove(menu);
        menu.dissociate();
    }

    public void updateMenu(Long menuId, String name, Long price) {
        this.menus.stream()
                .filter(it -> Objects.equals(it.getId(), menuId))
                .findAny()
                .ifPresent(it -> it.update(name, price));
    }
}
