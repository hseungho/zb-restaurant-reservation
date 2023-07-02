package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
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

    /**
     * 매장 검색 - 매장명 기준
     *
     * @param name     검색할 매장명
     * @param userX    요청자 x 좌표
     * @param userY    요청자 y 좌표
     * @param pageable 페이징 객체
     * @return 매장 DTO 클래스 리스트
     */
    Slice<IRestaurantDto> searchRestaurantByName(String name, String userX, String userY, Pageable pageable);

    /**
     * 매장 검색 - 매장위치 기준
     * @param address 검색할 매장 위치
     * @param userX 요청자 x 좌표
     * @param userY 요청자 y 좌표
     * @param pageable 페이징 객체
     * @return 매장 DTO 클래스 리스트
     */
    Slice<IRestaurantDto> searchRestaurantByAddress(String address, String userX, String userY, Pageable pageable);

    /**
     * 매장 상세정보 조회 메소드.
     * @param id 조회할 매장 ID
     * @return 매장 DTO 클래스
     */
    RestaurantDto findById(Long id);

    /**
     * 매장 정보 수정 메소드.
     *
     * @param restaurantId 정보 수정할 매장 ID
     * @param request      매장 정보 수정 요청 DTO 클래스
     * @return 매장 DTO 클래스
     */
    RestaurantDto updateRestaurant(Long restaurantId, UpdateRestaurant.Request request);

    /**
     * 매장 삭제 메소드.
     * @param restaurantId 삭제할 매장 ID
     * @return 매장 DTO 클래스
     */
    RestaurantDto deleteRestaurant(Long restaurantId);

    /**
     * 매장 삭제 요청 메소드.
     * @param restaurantId 삭제 요청할 매장 ID
     * @param date 매장 삭제 진행할 일자
     * @return 매장 DTO 클래스
     */
    RestaurantDto requestDeletingRestaurant(Long restaurantId, LocalDate date);

    /**
     * 메뉴 추가 메소드.
     *
     * @param restaurantId 메뉴 추가할 매장 ID
     * @param request      메뉴 추가 요청 DTO 클래스
     * @return 매장 DTO 클래스
     */
    RestaurantDto addMenus(Long restaurantId, AddMenus.Request request);

    /**
     * 메뉴 수정 메소드.
     * @param restaurantId 메뉴 수정할 매장 ID
     * @param menuId 수정할 메뉴 ID
     * @param request 메뉴 수정 요청 DTO 클래스
     * @return 메뉴 DTO 클래스
     */
    MenuDto updateMenu(Long restaurantId, Long menuId, UpdateMenu.Request request);

    /**
     * 메뉴 삭제 메소드.
     * @param restaurantId 메뉴 삭제할 매장 ID
     * @param menuId 삭제할 메뉴 ID
     * @return 매장 DTO 클래스
     */
    RestaurantDto removeMenu(Long restaurantId, Long menuId);
}
