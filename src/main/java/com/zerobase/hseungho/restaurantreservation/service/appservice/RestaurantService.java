package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantService {

    /**
     * 매장 등록 메소드. <br>
     * <br>
     * 요청 정보를 이용하여 매장 Entity를 DB에 저장하고, <br>
     * 해당 매장 이름을 자동완성을 위한 Trie 객체에 추가하는 메소드다. <br>
     * <br>
     * @param request 매장 등록 요청 DTO 클래스
     * @return 매장 DTO 클래스
     */
    RestaurantDto saveRestaurant(SaveRestaurant.Request request);

    /**
     * 검색 자동완성 제공 메소드. <br>
     * <br>
     * 요청하는 키워드에 대해서 Trie 객체에 담겨 있는 매장 이름을 반환한다. <br>
     * <br>
     * @param keyword 검색할 키워드
     * @return 키워드와 관련된 매장명 리스트
     */
    List<String> searchAutoComplete(String keyword);

    /**
     * 매장 검색 메소드. - 매장명 기준 <br>
     * <br>
     * 매장명 기준으로 DB의 매장 정보를 조회하는 메소드이며, <br>
     * 요청자의 좌표와 각 매장 간의 거리를 연산한 값을 포함한 매장 정보를 Slice 객체로 반환한다. <br>
     * 이 때, 정렬 방식으로는 매장이름과 평점, 거리 속성이 가능하다. <br>
     * <br>
     * @param name     검색할 매장명
     * @param userX    요청자 x 좌표
     * @param userY    요청자 y 좌표
     * @param pageable 페이징 객체
     * @return 매장 DTO 클래스 리스트
     */
    Slice<IRestaurantDto> searchRestaurantByName(String name, String userX, String userY, Pageable pageable);

    /**
     * 매장 검색 메소드. - 매장위치 기준 <br>
     * <br>
     * 매장위치(주소) 기준으로 DB의 매장 정보를 조회하는 메소드이며, <br>
     * 요청자의 좌표와 각 매장 간의 거리를 연산한 값을 포함한 매장 정보를 Slice 객체로 반환한다. <br>
     * 이 때, 정렬 방식으로는 매장이름과 평점, 거리 속성이 가능하다. <br>
     * <br>
     * @param address 검색할 매장 위치
     * @param userX 요청자 x 좌표
     * @param userY 요청자 y 좌표
     * @param pageable 페이징 객체
     * @return 매장 DTO 클래스 리스트
     */
    Slice<IRestaurantDto> searchRestaurantByAddress(String address, String userX, String userY, Pageable pageable);

    /**
     * 매장 상세정보 조회 메소드. <br>
     * <br>
     * 매장 ID를 이용하여 DB에서 매장 정보를 조회하는 메소드다. <br>
     * <br>
     * 단, 매장이 영업 종료된 경우, Bad_Request 에러를 반환한다. <br>
     * <br>
     * @param id 조회할 매장 ID
     * @return 매장 DTO 클래스
     */
    RestaurantDto findById(Long id);

    /**
     * 파트너의 내 매장 상세정보 조회 메소드. <br>
     * <br>
     * 파트너는 단 하나의 매장 밖에 가질 수 없는 정책에 따라 <br>
     * 요청자를 기준으로 매장을 DB에서 조회하여 반환하는 메소드다. <br>
     * <br>
     * 단, 매장이 영업 종료된 경우, Bad_Request 에러를 반환한다. <br>
     * <br>
     * @return 매장 DTO 클래스
     */
    RestaurantDto findByPartner();

    /**
     * 매장 정보 수정 메소드. <br>
     * <br>
     * 매장 정보를 수정하는 메소드로, 요청하는 정보에 따라 모든 매장 정보를 변경한다. <br>
     * <br>
     * 단, kakao API 호출 비용 때문에, 기존 매장 주소와 변경 요청 주소가 다를 경우에만 <br>
     * kakao API를 호출하여 좌표를 갱신한다. <br>
     * <br>
     * @param restaurantId 정보 수정할 매장 ID
     * @param request      매장 정보 수정 요청 DTO 클래스
     * @return 매장 DTO 클래스
     */
    RestaurantDto updateRestaurant(Long restaurantId, UpdateRestaurant.Request request);

    /**
     * 매장 삭제 메소드. <br>
     * <br>
     * 매장을 현재시간을 기준으로 삭제하는 메소드로, 매장 ID를 이용하여 DB에서 매장 정보를 조회하여, <br>
     * 매장 삭제에 대한 검증 로직을 거친 후, 매장의 삭제일자를 현재시간으로 변경하여 삭제된 매장으로 변경한다. <br>
     * 매장 정보를 DB 상에서 삭제하는 것이 아닌 Soft Delete 방식으로 삭제일시를 저장시키도록 한다. <br>
     * <br>
     * 단, 해당 매장에 이미지가 존재하는 리뷰가 있을 경우, 파일 스토리지에 저장되어 있는 파일도 삭제한다. <br>
     * <br>
     * @param restaurantId 삭제할 매장 ID
     * @return 매장 DTO 클래스
     */
    RestaurantDto deleteRestaurant(Long restaurantId);

    /**
     * 매장 삭제 요청 메소드. <br>
     * <br>
     * 현재 매장에 남은 예약이 존재할 경우, 매장을 즉시 삭제할 수 없기 떄문에, 점장은 특정 일자에 삭제을 요청할 수 있다. <br>
     * 매장 삭제 요청에 대한 검증 로직을 거친 후, 매장의 삭제요청일시를 요청시간으로 변경한다. <br>
     * <br>
     * @param restaurantId 삭제 요청할 매장 ID
     * @param date 매장 삭제 진행할 일자
     * @return 매장 DTO 클래스
     */
    RestaurantDto requestDeletingRestaurant(Long restaurantId, LocalDate date);

    /**
     * 메뉴 추가 메소드. <br>
     * <br>
     * 매장의 메뉴를 추가하여 DB에 저장하는 메소드다. <br>
     * 메뉴 추가에 대한 검증 로직을 거친 후, 해당 매장과 관계되는 새 메뉴 인스턴스를 생성하여 <br>
     * DB에 저장한다. <br>
     * <br>
     * @param restaurantId 메뉴 추가할 매장 ID
     * @param request      메뉴 추가 요청 DTO 클래스
     * @return 매장 DTO 클래스
     */
    RestaurantDto addMenus(Long restaurantId, AddMenus.Request request);

    /**
     * 메뉴 수정 메소드. <br>
     * <br>
     * 메뉴 정보를 수정하는 메소드로, 매장 ID를 이용하여 매장 정보를 조회하고, <br>
     * 메뉴 수정에 대한 검증 로직을 거친 후, 해당 메뉴에 대한 모든 정보를 수정 요청 정보로 변경한다. <br>
     * <br>
     * 이 때, 한 매장 단위로 메뉴의 정보는 많지 않을 것으로 예상되기 때문에, DB에서 메뉴 정보를 조회하는 것이 아니라 <br>
     * 매장의 메뉴 리스트를 스트림 API를 통해 탐색하여 해당 메뉴 정보를 변경하는 것으로 하였다. <br>
     * <br>
     * @param restaurantId 메뉴 수정할 매장 ID
     * @param menuId       수정할 메뉴 ID
     * @param request      메뉴 수정 요청 DTO 클래스
     * @return 메뉴 DTO 클래스
     */
    RestaurantDto updateMenu(Long restaurantId, Long menuId, UpdateMenu.Request request);

    /**
     * 메뉴 삭제 메소드. <br>
     * <br>
     * 메뉴를 삭제하는 메소드로, 메뉴 ID와 매장을 기준으로 DB에서 메뉴를 조회하고, <br>
     * 메뉴 삭제에 대한 검증 로직을 거친 후, 해당 메뉴를 DB 상에서 삭제한다. <br>
     * <br>
     * @param restaurantId 메뉴 삭제할 매장 ID
     * @param menuId 삭제할 메뉴 ID
     * @return 매장 DTO 클래스
     */
    RestaurantDto removeMenu(Long restaurantId, Long menuId);

}
