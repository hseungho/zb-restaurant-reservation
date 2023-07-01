package com.zerobase.hseungho.restaurantreservation.service.controller;

import com.zerobase.hseungho.restaurantreservation.service.appservice.ReviewService;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.SaveReview;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.UpdateReview;
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

    @PutMapping("${service.api.review.update}")
    @ResponseStatus(HttpStatus.OK)
    public UpdateReview.Response updateReview(@PathVariable("restaurantId") Long restaurantId,
                                              @PathVariable("reviewId") Long reviewId,
                                              @RequestBody @Valid UpdateReview.Request request) {
        return UpdateReview.Response.fromDto(
                reviewService.update(restaurantId, reviewId, request)
        );
    }
}
