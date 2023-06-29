package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.review.ReviewDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.SaveReview;

public interface ReviewService {

    /**
     * 리뷰 등록 메소드.
     * @param restaurantId 리뷰 등록할 매장 ID
     * @param request 리뷰 등록 요청 DTO 클래스
     * @return 리뷰 DTO 클래스
     */
    ReviewDto save(Long restaurantId, SaveReview.Request request);

}
