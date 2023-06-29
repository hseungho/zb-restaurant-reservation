package com.zerobase.hseungho.restaurantreservation.global.adapter.webclient;

import com.zerobase.hseungho.restaurantreservation.global.adapter.webclient.dto.KakaoMapResponse;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.util.ValidUtils;
import com.zerobase.hseungho.restaurantreservation.global.adapter.webclient.dto.CoordinateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class KakaoWebClientComponent {

    private final WebClient webClient;

    public KakaoWebClientComponent(Environment env) {
        final String API_KEY = env.getProperty("kakao.api-key");
        final String KAKAO_AK_PREFIX = "KakaoAK ";
        final String KAKAO_AUTHORIZATION = KAKAO_AK_PREFIX + API_KEY;

        webClient = WebClient.builder()
                .baseUrl("https://dapi.kakao.com/v2")
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.set(HttpHeaders.AUTHORIZATION, KAKAO_AUTHORIZATION);
                })
                .build();
    }

    public CoordinateDto getCoordinateByAddress(String address) {
        KakaoMapResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/local/search/address")
                        .queryParam("query", address)
                        .build())
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        statusResponse -> statusResponse.bodyToMono(String.class).map(RuntimeException::new)
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        statusResponse -> statusResponse.bodyToMono(String.class).map(RuntimeException::new)
                )
                .bodyToMono(KakaoMapResponse.class)
                .block();

        KakaoMapResponse.KakaoMapDocument kakaoMapDocument = validateResponseAndIfPresentGetDocument(response, address);

        return new CoordinateDto(
                Double.parseDouble(kakaoMapDocument.getX()),
                Double.parseDouble(kakaoMapDocument.getY())
        );
    }

    private KakaoMapResponse.KakaoMapDocument validateResponseAndIfPresentGetDocument(KakaoMapResponse response, String address) {
        if (response == null || CollectionUtils.isEmpty(response.getDocuments())) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_RESTAURANT_COORDINATE_BY_ADDRESS);
        }
        return response.getDocuments().stream()
                .filter(e -> ValidUtils.isSimilarBetweenText(address, e.getAddress_name(), 0.7))
                .findAny()
                .orElseThrow(() -> new BadRequestException(ErrorCodeType.BAD_REQUEST_SAVE_RESTAURANT_COORDINATE_BY_ADDRESS));
    }

}
