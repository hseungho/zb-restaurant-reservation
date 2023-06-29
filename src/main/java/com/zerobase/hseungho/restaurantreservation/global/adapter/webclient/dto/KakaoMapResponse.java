package com.zerobase.hseungho.restaurantreservation.global.adapter.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoMapResponse {
    private List<KakaoMapDocument> documents;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoMapDocument {
        private String address_name;
        private String x;
        private String y;
    }
}
