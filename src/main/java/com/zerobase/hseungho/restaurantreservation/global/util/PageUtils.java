package com.zerobase.hseungho.restaurantreservation.global.util;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

public class PageUtils {

    /**
     * 매장 리스트 검색 시 쿼리를 요청할 때, <br>
     * 정렬 속성으로는 rating 과 name, distance 를 활용하는데, 기존 Pageable 로 쿼리를 요청하면, <br>
     * DB의 Attribute 가 아닌 계산된 더미 변수인 distance 에 대한 퀴리가 restaurant.distance 로 <br>
     * 설정됨에 따라 쿼리 예외가 발생한다. <br>
     * <br>
     * 따라서 요청의 Pageable 에 대해서 적절한 정렬 속성을 지정하여 PageRequest 를 반환한다.
     * @param pageable 요청단의 Pageable
     * @return PageRequest 객체
     */
    public static PageRequest of(Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        Sort.Order order = pageable.getSort().iterator().next();
        Sort sort = switch (order.getProperty()) {
            case "rating", "name" -> Sort.by(order.getDirection(), order.getProperty());
            case "distance" -> JpaSort.unsafe(order.getDirection(), "CAST(distance AS double)");
            default -> throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SEARCH_RESTAURANT_INVALID_SORT_PROPERTY);
        };
        return PageRequest.of(page, size, sort);
    }

}
