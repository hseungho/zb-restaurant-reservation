package com.zerobase.hseungho.restaurantreservation.service.controller;

import com.zerobase.hseungho.restaurantreservation.service.appservice.ReviewService;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.DeleteReview;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.SaveReview;
import com.zerobase.hseungho.restaurantreservation.service.dto.review.UpdateReview;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${service.api.prefix}")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("${service.api.review.save}")
    @ResponseStatus(HttpStatus.CREATED)
    public SaveReview.Response saveReview(@PathVariable("restaurantId") Long restaurantId,
                                          @RequestPart("data") @Valid SaveReview.Request request,
                                          @RequestPart("image") MultipartFile image) {
        return SaveReview.Response.fromDto(
                reviewService.save(restaurantId, request, image)
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

    @DeleteMapping("${service.api.review.delete}")
    @ResponseStatus(HttpStatus.OK)
    public DeleteReview.Response deleteReview(@PathVariable("restaurantId") Long restaurantId,
                                              @PathVariable("reviewId") Long reviewId) {
        return DeleteReview.Response.of(
                reviewService.delete(restaurantId, reviewId)
        );
    }
}
