package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.review.ReviewDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.SaveReview;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.UpdateReview;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewService {

    /**
     * 리뷰 등록 메소드.
     *
     * @param restaurantId 리뷰 등록할 매장 ID
     * @param request      리뷰 등록 요청 DTO 클래스
     * @param image
     * @return 리뷰 DTO 클래스
     */
    ReviewDto save(Long restaurantId, SaveReview.Request request, MultipartFile image);

    /**
     * 리뷰 수정 메소드.
     *
     * @param restaurantId 리뷰 수정할 매장 ID
     * @param reviewId     리뷰 수정할 리뷰 ID
     * @param request      리뷰 수정 요청 DTO 클래스
     * @param image
     * @return 리뷰 DTO 클래스
     */
    ReviewDto update(Long restaurantId, Long reviewId, UpdateReview.Request request, MultipartFile image);

    /**
     * 리뷰 삭제 메소드.
     * @param restaurantId 리뷰 삭제할 매장 ID
     * @param reviewId 리뷰 수정할 리뷰 ID
     * @return 리뷰 삭제한 매장 ID
     */
    Long delete(Long restaurantId, Long reviewId);
}
