package com.zerobase.hseungho.restaurantreservation.global.security;

import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtComponent {

    private static final String CLAIMS_TYPE = "type";
    private static final long ACCESS_TOKEN_EXPIRED_TIME = 1000 * 60 * 60;  // 1 hour
    private static final long REFRESH_TOKEN_EXPIRED_TIME = 1000 * 60 * 60;  // 1 hour

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private final UserAuthenticationService userAuthenticationService;

    public String generateAccessToken(String id, UserType type) {
        return generateToken(id, type, ACCESS_TOKEN_EXPIRED_TIME);
    }

    public String generateRefreshToken(String id, UserType type) {
        return generateToken(id, type, REFRESH_TOKEN_EXPIRED_TIME);
    }

    private String generateToken(String id, UserType type, long expired) {
        Claims claims = Jwts.claims().setSubject(id);
        claims.put(CLAIMS_TYPE, type.name());

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRED_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) return false;

        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userAuthenticationService.loadUserByUsername(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserId(String token) {
        return this.parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody();
    }


}
