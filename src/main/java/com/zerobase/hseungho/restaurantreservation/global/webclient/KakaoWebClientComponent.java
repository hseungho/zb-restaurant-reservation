package com.zerobase.hseungho.restaurantreservation.global.webclient;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.webclient.dto.CoordinateDto;
import com.zerobase.hseungho.restaurantreservation.global.webclient.dto.KakaoMapResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
public class KakaoWebClientComponent {

    private final WebClient webClient;

    public KakaoWebClientComponent() {
        webClient = WebClient.builder()
                .baseUrl("https://dapi.kakao.com/v2")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public CoordinateDto getCoordinateByAddress(String address) {
        KakaoMapResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/local/search/address")
                        .queryParam("query", address)
                        .build())
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                        return clientResponse.bodyToMono(KakaoMapResponse.class);
                    } else {
                        return clientResponse.createException().flatMap(Mono::error);
                    }
                })
                .block();

        validateResponse(response);

        KakaoMapResponse.KakaoMapDocument kakaoMapDocument = response.getDocuments().stream()
                .filter(e -> Objects.equals(address, e.getAddress_name()))
                .findAny()
                .orElseThrow(() -> new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_RESTAURANT_COORDINATE_BY_ADDRESS));

        return new CoordinateDto(
                Double.parseDouble(kakaoMapDocument.getX()),
                Double.parseDouble(kakaoMapDocument.getY())
        );
    }

    private void validateResponse(KakaoMapResponse response) {
        if (response == null || CollectionUtils.isEmpty(response.getDocuments())) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_RESTAURANT_COORDINATE_BY_ADDRESS);
        }
    }

}
