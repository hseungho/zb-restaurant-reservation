package com.zerobase.hseungho.restaurantreservation.service.dto.reservation;

import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.restaurant.RestaurantTimeDto;
import com.zerobase.hseungho.restaurantreservation.service.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

public class FindReservationList {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private ResponseContent content;
        private Integer number;
        private Integer size;
        private Integer numberOfElements;
        private Boolean isFirst;
        private Boolean isLast;
        private Boolean hasNext;
        private Boolean hasPrevious;
        public static Response fromDto(Slice<ReservationDto> dtos) {
            return Response.builder()
                    .content(ResponseContent.fromDto(dtos.getContent()))
                    .number(dtos.getNumber())
                    .size(dtos.getSize())
                    .numberOfElements(dtos.getNumberOfElements())
                    .isFirst(dtos.isFirst())
                    .isLast(dtos.isLast())
                    .hasNext(dtos.hasNext())
                    .hasPrevious(dtos.hasPrevious())
                    .build();
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        private static class ResponseContent {
            private List<ResponseDocument> documents;
            public static ResponseContent fromDto(List<ReservationDto> dto) {
                return ResponseContent.builder()
                        .documents(dto.stream().map(ResponseDocument::fromDto).toList())
                        .build();
            }

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder
            private static class ResponseDocument {
                private RestaurantResponse restaurant;
                private ReservationResponse reservation;
                private static ResponseDocument fromDto(ReservationDto dto) {
                    return ResponseDocument.builder()
                            .restaurant(RestaurantResponse.fromDto(dto.getRestaurant()))
                            .reservation(ReservationResponse.fromDto(dto))
                            .build();
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @Builder
                private static class RestaurantResponse {
                    private Long id;
                    private String name;
                    private String address;
                    private String description;
                    private RestaurantTimeDto openTime;
                    private RestaurantTimeDto closeTime;
                    private String contactNumber;
                    private static RestaurantResponse fromDto(RestaurantDto dto) {
                        return RestaurantResponse.builder()
                                .id(dto.getId())
                                .name(dto.getName())
                                .address(dto.getAddress())
                                .description(dto.getDescription())
                                .openTime(RestaurantTimeDto.of(dto.getOpenTime().getHour(), dto.getCloseTime().getMinute()))
                                .closeTime(RestaurantTimeDto.of(dto.getCloseTime().getHour(), dto.getCloseTime().getMinute()))
                                .contactNumber(dto.getContactNumber())
                                .build();
                    }
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @Builder
                private static class ReservationResponse {
                    private Long id;
                    private String number;
                    private String reservedAt;
                    private String canceledAt;
                    private String approvedAt;
                    private String refusedAt;
                    private String visitedAt;
                    private Integer numOfPerson;
                    private String clientContactNumber;
                    private String status;
                    private String createdAt;
                    private static ReservationResponse fromDto(ReservationDto dto) {
                        return ReservationResponse.builder()
                                .id(dto.getId())
                                .number(dto.getNumber())
                                .reservedAt(dto.getReservedAt().toString())
                                .canceledAt(dto.getStatus() == ReservationStatus.CANCELED ?
                                        dto.getCanceledAt().toString() : null)
                                .approvedAt(dto.getStatus() == ReservationStatus.APPROVED || dto.getStatus() == ReservationStatus.VISITED ?
                                        dto.getApprovedAt().toString() : null)
                                .refusedAt(dto.getStatus() == ReservationStatus.REFUSED ?
                                        dto.getRefusedAt().toString() : null)
                                .visitedAt(dto.getStatus() == ReservationStatus.VISITED ?
                                        dto.getVisitedAt().toString() : null)
                                .numOfPerson(dto.getNumOfPerson())
                                .clientContactNumber(dto.getClientContactNumber())
                                .status(dto.getStatus().name())
                                .createdAt(dto.getCreatedAt().toString())
                                .build();
                    }

                }
            }
        }
    }
}
