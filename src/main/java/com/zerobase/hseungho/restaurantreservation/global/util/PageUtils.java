package com.zerobase.hseungho.restaurantreservation.global.util;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

public class PageUtils {

    public static PageRequest of(Pageable pageable) {
        long offset = pageable.getOffset();
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
