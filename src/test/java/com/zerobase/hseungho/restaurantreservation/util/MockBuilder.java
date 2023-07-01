package com.zerobase.hseungho.restaurantreservation.util;

import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import com.zerobase.hseungho.restaurantreservation.service.domain.reservation.Reservation;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.AddressVO;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Menu;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Review;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.type.ReservationStatus;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MockBuilder {
    public static final String MOCK_USER_ID = "testid";
    public static final String MOCK_PASSWORD = "testpassword1234!";
    public static final String MOCK_NICKNAME = "testnickname";
    public static User mockUser(UserType type) {
        return User.builder()
                .userId(MOCK_USER_ID)
                .password(MOCK_PASSWORD)
                .nickname(MOCK_NICKNAME)
                .type(type)
                .build();
    }

    public static final Long MOCK_RESTAURANT_ID = 1L;
    public static final String MOCK_RESTAURANT_NAME = "매장이름";
    public static final String MOCK_ADDRESS = "서울 서대문구 증가로 12";
    public static final String MOCK_DESCRIPTION = "매장 설명입니다.";
    public static final String MOCK_MENUNAME_1 = "메뉴1";
    public static final Long MOCK_MENUPRICE_1 = 10000L;
    public static final String MOCK_MENUNAME_2 = "메뉴2";
    public static final Long MOCK_MENUPRICE_2 = 20000L;
    public static final int MOCK_OPEN_HOUR = 10;
    public static final int MOCK_OPEN_MINUTE = 0;
    public static final int MOCK_CLOSE_HOUR = 22;
    public static final int MOCK_CLOSE_MINUTE = 0;
    public static final int MOCK_COUNT_OF_TABLES = 10;
    public static final int MOCK_MAX_PER_RESERVATION = 4;
    public static final String MOCK_CONTACT_NUMBER = "021231234";
    public static final Double MOCK_RATING = 4.5;
    public static Restaurant mockRestaurant(User manager) {
        return Restaurant.builder()
                .id(MOCK_RESTAURANT_ID)
                .name(MOCK_RESTAURANT_NAME)
                .addressVO(new AddressVO(MOCK_ADDRESS, 34.123, 123.314))
                .description(MOCK_DESCRIPTION)
                .menus(List.of(
                        Menu.builder()
                                .name(MOCK_MENUNAME_1).price(MOCK_MENUPRICE_1).build(),
                        Menu.builder()
                                .name(MOCK_MENUNAME_2).price(MOCK_MENUPRICE_2).build()
                ))
                .open(LocalTime.of(MOCK_OPEN_HOUR, MOCK_OPEN_MINUTE))
                .close(LocalTime.of(MOCK_CLOSE_HOUR, MOCK_CLOSE_MINUTE))
                .countOfTables(MOCK_COUNT_OF_TABLES)
                .maxPerReservation(MOCK_MAX_PER_RESERVATION)
                .contactNumber(MOCK_CONTACT_NUMBER)
                .rating(MOCK_RATING)
                .reviews(new ArrayList<>())
                .manager(manager)
                .build();
    }

    public static final Long MOCK_RESERVATION_ID = 1L;
    public static final String MOCK_RESERVATION_NUMBER = "ABCDE12345";
    public static final int MOCK_NUM_OF_PERSON = 4;
    public static final String MOCK_CLIENT_CONTACT_NUMBER = "01012341234";

    public static Reservation mockReservation(LocalDateTime reservedAt,
                                              ReservationStatus status,
                                              User client,
                                              Restaurant restaurant) {
        return Reservation.builder()
                .id(MOCK_RESERVATION_ID)
                .number(MOCK_RESERVATION_NUMBER)
                .numOfPerson((MOCK_NUM_OF_PERSON))
                .clientContactNumber((MOCK_CLIENT_CONTACT_NUMBER))
                .createdAt(SeoulDateTime.now())
                .reservedAt(reservedAt)
                .status(status)
                .client(client)
                .restaurant(restaurant)
                .build();
    }

    public static final Long MOCK_REVIEW_ID = 1L;
    public static final Double MOCK_REVIEW_RATING = 4.5;
    public static final String MOCK_REVIEW_CONTENT = "리뷰내용입니다.";
    public static final String MOCK_REVIEW_IMAGE_SRC = "리뷰이미지주소";
    public static final Review mockReview(User author, Restaurant restaurant) {
        return Review.builder()
                .id(MOCK_REVIEW_ID)
                .rating(MOCK_REVIEW_RATING)
                .content(MOCK_REVIEW_CONTENT)
                .imageSrc(MOCK_REVIEW_IMAGE_SRC)
                .author(author)
                .restaurant(restaurant)
                .build();

    }
}
