package com.zerobase.hseungho.restaurantreservation.global.security.jwt;

import com.zerobase.hseungho.restaurantreservation.global.security.UserAuthenticationComponent;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static com.zerobase.hseungho.restaurantreservation.global.constants.TokenConstants.*;

/**
 * JwtComponent 는 JWT 토큰을 관리하기 위한 클래스. <br>
 * 토큰을 생성하고, 검증한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtComponent {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private final UserAuthenticationComponent userAuthenticationComponent;

    /**
     * 해당 유저에 대한 액세스 토큰을 생성하는 메소드.
     * @param id 해당 유저의 PK ID
     * @param type 해당 유저의 UserType
     * @return 생성된 JWT 액세스 토큰
     */
    public String generateAccessToken(String id, UserType type) {
        return TOKEN_PREFIX + generateToken(id, type, ACCESS_TOKEN_EXPIRED_TIME);
    }

    /**
     * 해당 유저의 리프레시 토큰을 생성하는 메소드.
     * @param id 해당 유저의 PK ID
     * @param type 해당 유저의 UserType
     * @return 생성된 JWT 리프레시 토큰
     */
    public String generateRefreshToken(String id, UserType type) {
        return TOKEN_PREFIX + generateToken(id, type, REFRESH_TOKEN_EXPIRED_TIME);
    }

    /**
     * JWT 토큰을 생성하는 메소드.
     * @param id 유저의 PK ID
     * @param type 유저의 UserType
     * @param expired 토큰의 만료시간
     * @return 생성된 JWT 토큰
     */
    private String generateToken(String id, UserType type, long expired) {
        Claims claims = Jwts.claims().setSubject(id);
        claims.put(CLAIMS_ROLE, type.name());

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expired);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, this.getBase64SecretKey())
                .compact();
    }

    /**
     * JWT 토큰을 검증하는 메소드.
     * @param token 검증할 토큰
     * @return 검증 결과 값. 검증 통과하면 true.
     */
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) return false;

        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    /**
     * 토큰 Payload 에 담겨있는 유저 PK ID를 이용하여 DB를 조회한 뒤, <br>
     * UsernamePasswordAuthenticationToken 을 반환하는 메소드.
     * @param token 토큰 값
     * @return UsernamePasswordAuthenticationToken 로 객체화한 Authentication 인터페이스
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userAuthenticationComponent.loadUserByUsername(this.getId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    /**
     * 토큰 Payload 에 담겨있는 유저 PK ID를 반환하는 메소드.
     * @param token 토큰 값
     * @return 유저 PK ID
     */
    public String getId(String token) {
        return this.parseClaims(token).getSubject();
    }

    /**
     * JWT 토큰을 파싱하는 메소드.
     * @param token 파싱할 토큰 값
     * @return JWT Claims
     */
    private Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(this.getBase64SecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 설정한 Secret Key 를 Base64로 인코딩한 값을 반환하는 메소드.
     * @return 인코딩된 Secret Key
     */
    private String getBase64SecretKey() {
        return Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 토큰 Prefix 인 Bearer 을 제거하여 반환하는 메소드.
     * @param authorization Prefix 를 제거하고자 하는 값
     * @return Prefix 를 제거한 토큰 값
     */
    public String separatePrefix(String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith(TOKEN_PREFIX)) {
            return authorization.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

}
