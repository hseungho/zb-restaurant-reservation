package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.adapter.fileupload.AwsS3ImageUpload;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.ForbiddenException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.InternalServerErrorException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.NotFoundException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.security.SecurityHolder;
import com.zerobase.hseungho.restaurantreservation.global.util.ValidUtils;
import com.zerobase.hseungho.restaurantreservation.service.domain.reservation.Reservation;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Review;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.ReviewDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.SaveReview;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.UpdateReview;
import com.zerobase.hseungho.restaurantreservation.service.repository.ReservationRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.ReviewRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    private final AwsS3ImageUpload uploader;

    @Override
    @Transactional
    public ReviewDto save(Long restaurantId, SaveReview.Request request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESERVATION));
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));

        validateSaveRequest(restaurant, reservation, user, request);

        Review review = Review.create(
                request.getRating(),
                request.getContent(),
                uploadImageIfPresent(request.getImage()),
                user
        );

        restaurant.addReview(review);

        return ReviewDto.fromEntityWithReservation(review, reservation);
    }

    @Override
    @Transactional
    public ReviewDto update(Long restaurantId, Long reviewId, UpdateReview.Request request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_REVIEW));

        validateUpdateRequest(restaurant, review, request);

        review.update(
                request.getRating(),
                request.getContent(),
                updateImage(review, request)
        );

        return ReviewDto.fromEntity(review);
    }

    @Override
    public Long delete(Long restaurantId, Long reviewId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_RESTAURANT));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_REVIEW));

        validateDeleteRequest(restaurant, review);

        reviewRepository.delete(review);
        
        return restaurantId;
    }

    private void validateDeleteRequest(Restaurant restaurant, Review review) {
        if (restaurant.isDeleted()) {
            // 영업종료된 매장의 리뷰를 삭제할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_DELETE_REVIEW_DELETED_RESTAURANT);
        }
        if (!review.isAuthorId(SecurityHolder.getIdOfUser())) {
            // 리뷰의 작성자가 아닌 유저가 리뷰를 삭제할 수 없습니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_DELETE_REVIEW_NOT_YOUR_REVIEW);
        }
    }

    private String updateImage(Review review, UpdateReview.Request request) {
        if (request.getIsDeleteImage()) {
            uploader.delete(review.getImageSrc());
            return null;
        }
        if (request.getIsUpdateImage() && request.getImage() != null) {
            uploader.delete(review.getImageSrc());
            return this.uploadImageIfPresent(request.getImage());
        }
        return review.getImageSrc();
    }

    private void validateUpdateRequest(Restaurant restaurant, Review review, UpdateReview.Request request) {
        if (!ValidUtils.isMin(1.0, request.getRating()) || !ValidUtils.isMax(5.0, request.getRating())) {
            // 평점은 1~5점 사이의 점수만 줄 수 있습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_UPDATE_REVIEW_INVALID_RATING_RANGE);
        }
        if (!ValidUtils.hasTexts(request.getContent())) {
            // 리뷰 수정에 필요한 모든 정보를 입력해야합니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_UPDATE_REVIEW_BLANK);
        }
        if (restaurant.isDeleted()) {
            // 영업종료된 매장의 리뷰를 수정할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_UPDATE_REVIEW_DELETED_RESTAURANT);
        }
        if (!review.isAuthorId(SecurityHolder.getIdOfUser())) {
            // 리뷰의 작성자가 아닌 유저가 리뷰를 수정할 수 없습니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_UPDATE_REVIEW_NOT_YOUR_REVIEW);
        }
    }

    private String uploadImageIfPresent(MultipartFile image) {
        try {
            return uploader.upload(image);
        } catch (IOException e) {
            throw new InternalServerErrorException(ErrorCodeType.INTERNAL_SERVER_ERROR_UPLOAD_IMAGE_S3);
        }
    }

    private void validateSaveRequest(Restaurant restaurant, Reservation reservation, User user, SaveReview.Request request) {
        if (!ValidUtils.isMin(1.0, request.getRating()) || !ValidUtils.isMax(5.0, request.getRating())) {
            // 평점은 1~5점 사이의 점수만 줄 수 있습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_REVIEW_INVALID_RATING_RANGE);
        }
        if (!ValidUtils.hasTexts(request.getContent())) {
            // 리뷰 등록에 필요한 모든 정보를 입력해야합니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_REVIEW_BLANK);
        }
        if (restaurant.isDeleted()) {
            // 영업종료된 매장에 리뷰를 등록할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_REVIEW_DELETED_RESTAURANT);
        }
        if (!reservation.isClient(user)) {
            // 해당 예약의 예약자가 아닙니다.
            throw new ForbiddenException(ErrorCodeType.FORBIDDEN_SAVE_REVIEW_NOT_YOUR_RESOURCE);
        }
        if (ValidUtils.isAfterNow(reservation.getReservedAt())) {
            // 예약시간보다 이전에 리뷰를 등록할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_REVIEW_CANNOT_BEFORE_RESERVED_TIME);
        }
        if (!reservation.isVisited()) {
            // 도착확인이 되기 전에 리뷰를 등록할 수 없습니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_REVIEW_CAN_ONLY_VISITED);
        }
    }
}