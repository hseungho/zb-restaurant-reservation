package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.adapter.fileupload.AwsS3ImageManager;
import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import com.zerobase.hseungho.restaurantreservation.service.domain.reservation.Reservation;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Review;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.ReviewDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.SaveReview;
import com.zerobase.hseungho.restaurantreservation.service.repository.ReservationRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.ReviewRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import com.zerobase.hseungho.restaurantreservation.service.type.ReservationStatus;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import com.zerobase.hseungho.restaurantreservation.util.MockBuilder;
import com.zerobase.hseungho.restaurantreservation.util.TestSecurityHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplSaveUnitTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private AwsS3ImageManager uploader;

    @Test
    @DisplayName("리뷰 등록")
    void test_save_success() throws Exception {
        // given
        LocalDateTime reservedAt = SeoulDateTime.now().minusMinutes(5);

        User user = TestSecurityHolder.setSecurityHolderUser(UserType.ROLE_CUSTOMER);
        Restaurant restaurant = MockBuilder.mockRestaurant(MockBuilder.mockUser(UserType.ROLE_PARTNER));
        Reservation reservation = MockBuilder.mockReservation(reservedAt, ReservationStatus.VISITED, user, restaurant);
        given(restaurantRepository.findById(anyLong()))
                .willReturn(Optional.of(restaurant));
        given(reservationRepository.findById(anyLong()))
                .willReturn(Optional.of(reservation));
        given(userRepository.findById(anyString()))
                .willReturn(Optional.of(user));
        given(uploader.upload(any()))
                .willReturn(MockBuilder.MOCK_REVIEW_IMAGE_SRC);
        given(reviewRepository.save(any()))
                .willReturn(MockBuilder.mockReview(user, restaurant, reservation));
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        // when
        ReviewDto result = reviewService.save(1L, request(), new MockMultipartFile("test", new byte[]{}));
        // then
        verify(reviewRepository, times(1)).save(captor.capture());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(MockBuilder.MOCK_REVIEW_RATING, result.getRating());
        Assertions.assertEquals(MockBuilder.MOCK_REVIEW_CONTENT, result.getContent());
        Assertions.assertEquals(MockBuilder.MOCK_REVIEW_IMAGE_SRC, result.getImageSrc());
        Assertions.assertEquals(restaurant.getId(), result.getRestaurant().getId());
        Assertions.assertEquals(reservation.getId(), result.getReservation().getId());
    }

    private SaveReview.Request request() {
        return SaveReview.Request.builder()
                .reservationId(1L)
                .rating(MockBuilder.MOCK_REVIEW_RATING)
                .content(MockBuilder.MOCK_REVIEW_CONTENT)
                .build();
    }

}
