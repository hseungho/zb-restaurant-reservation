package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import com.zerobase.hseungho.restaurantreservation.service.domain.base.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "menu")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Menu extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "price", nullable = false)
    private Long price;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public static Menu create(String name, Long price) {
        Menu menu = new Menu();
        menu.name = name;
        menu.price = price;
        return menu;
    }

    public void associate(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void dissociate() {
        this.restaurant = null;
    }

    public void update(String name, Long price) {
        this.name = name;
        this.price = price;
    }
}
