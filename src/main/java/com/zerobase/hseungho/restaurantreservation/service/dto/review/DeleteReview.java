package com.zerobase.hseungho.restaurantreservation.service.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DeleteReview {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    @Builder
    public static class Response {
        private Long restaurantId;
    }
}
