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

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtComponent {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private final UserAuthenticationComponent userAuthenticationComponent;

    public String generateAccessToken(String id, UserType type) {
        return TOKEN_PREFIX + generateToken(id, type, ACCESS_TOKEN_EXPIRED_TIME);
    }

    public String generateRefreshToken(String id, UserType type) {
        return TOKEN_PREFIX + generateToken(id, type, REFRESH_TOKEN_EXPIRED_TIME);
    }

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

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) return false;

        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userAuthenticationComponent.loadUserByUsername(this.getId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public String getId(String token) {
        return this.parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(this.getBase64SecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    private String getBase64SecretKey() {
        return Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String separatePrefix(String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith(TOKEN_PREFIX)) {
            return authorization.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

}
