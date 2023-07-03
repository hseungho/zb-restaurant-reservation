package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.review.ReviewDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.SaveReview;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.UpdateReview;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewService {

    /**
     * 리뷰 등록 메소드. <br>
     * <br>
     * 새 리뷰를 저장하는 메소드로, 리뷰는 어떤 유저가 어떤 매장에, 어떤 예약에 대하여 작성하는지 <br>
     * 알아야 하기 때문에, DB에서 매장, 예약, 유저 정보를 조회하고, <br>
     * 이들과의 관계되도록 한 새 리뷰 인스턴스를 DB에 저장한다. <br>
     * <br>
     * 이 때, 리뷰 등록 요청에 이미지가 존재할 경우, 파일 스토리지에 업로드한 주소를 리뷰의 이미지 주소로 <br>
     * 지정하여 저장한다. <br>
     * <br>
     * @param restaurantId 리뷰 등록할 매장 ID
     * @param request      리뷰 등록 요청 DTO 클래스
     * @param image 리뷰 등록 시 업로드 할 이미지
     * @return 리뷰 DTO 클래스
     */
    ReviewDto save(Long restaurantId, SaveReview.Request request, MultipartFile image);

    /**
     * 리뷰 수정 메소드. <br>
     * <br>
     * 리뷰를 수정하는 메소드로, 리뷰 수정에 대한 검증 로직을 거친 후, 리뷰 정보를 수정한다. <br>
     * <br>
     * 이 때, 요청자가 이미지를 삭제할건지, 수정할건지에 따른 boolean 값을 함께 전달하기 때문에, <br>
     * 해당 boolean 값에 따라 파일 스토리지의 이미지 관리를 각각 수행하도록 한다. <br>
     * <br>
     * @param restaurantId 리뷰 수정할 매장 ID
     * @param reviewId     리뷰 수정할 리뷰 ID
     * @param request      리뷰 수정 요청 DTO 클래스
     * @param image 리뷰 수정 시 수정할 이미지
     * @return 리뷰 DTO 클래스
     */
    ReviewDto update(Long restaurantId, Long reviewId, UpdateReview.Request request, MultipartFile image);

    /**
     * 리뷰 삭제 메소드. <br>
     * <br>
     * 리뷰를 삭제하는 메소드로, 리뷰 삭제에 대한 검증 로직을 거친 후, 리뷰를 삭제한다. <br>
     * <br>
     * 이 때, 해당 리뷰에 이미지 주소가 존재할 경우, 파일 스토리지의 해당 이미지 또한 삭제한다. <br>
     * <br>
     * @param restaurantId 리뷰 삭제할 매장 ID
     * @param reviewId 리뷰 수정할 리뷰 ID
     * @return 리뷰 삭제한 매장 ID
     */
    Long delete(Long restaurantId, Long reviewId);
}
