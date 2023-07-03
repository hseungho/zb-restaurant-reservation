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

/**
 * Kakao API 를 WebClient 로 호출하여 응답하도록 하는 컴포넌트 클래스.
 */
@Slf4j
@Component
public class KakaoWebClientComponent {

    private final WebClient webClient;

    /**
     * KakaoWebClientComponent 의 생성자. <br>
     * <br>
     * WebClient 를 빌드하여 정의하고, 디폴트 헤더에 Key 값을 추가한다.
     * @param env Spring Environment
     */
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

    /**
     * 도로명 주소를 이용하여 Kakao 지도 API 를 호출하여 <br>
     * 해당 주소의 좌표를 반환하는 메소드.
     * @param address 좌표를 구할 도로명 주소
     * @return 좌표 DTO 클래스
     */
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

    /**
     * Kakao API 응답을 검증하고, 응답이 존재할 경우 응답값을 반환하는 메소드. <br>
     * <br>
     * 응답 모델 중 document 에 해당하는 값에 대해서 요청 도로명 주소와 응답 도로명 주소의 <br>
     * 유사도를 확인하고 이들이 70% 이상 유사한 document 를 반환한다.
     * @param response Kakao API 응답 모델
     * @param address 요청 도로명 주소
     * @return Kakao API 응답 모델 중 document 객체
     */
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
