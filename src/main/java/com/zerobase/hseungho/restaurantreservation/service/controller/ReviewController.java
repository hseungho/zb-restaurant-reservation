package com.zerobase.hseungho.restaurantreservation.service.controller;

import com.zerobase.hseungho.restaurantreservation.service.appservice.ReviewService;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.SaveReview;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${service.api.prefix}")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("${service.api.review.save}")
    @ResponseStatus(HttpStatus.CREATED)
    public SaveReview.Response saveReview(@PathVariable("restaurantId") Long restaurantId,
                                          @RequestBody @Valid SaveReview.Request request) {
        return SaveReview.Response.fromDto(
                reviewService.save(restaurantId, request)
        );
    }

}
