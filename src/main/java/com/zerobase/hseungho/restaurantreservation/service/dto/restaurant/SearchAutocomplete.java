package com.zerobase.hseungho.restaurantreservation.service.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SearchAutocomplete {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String result;
    }
}
