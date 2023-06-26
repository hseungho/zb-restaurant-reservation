package com.zerobase.hseungho.restaurantreservation.service.domain.restaurant;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddressVO {

    private String address;
    private Double x;
    private Double y;

}
