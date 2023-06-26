package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.SaveRestaurant;

import java.util.List;

public interface RestaurantService {

    /**
     * 매장 등록 메소드.
     * @param request 매장 등록 요청 DTO 클래스
     * @return 매장 DTO 클래스
     */
    RestaurantDto saveRestaurant(SaveRestaurant.Request request);

    /**
     * 검색 자동완성 제공 메소드.
     * @param keyword 검색할 키워드
     * @return 키워드와 관련된 매장명 리스트
     */
    List<String> searchAutoComplete(String keyword);

}
