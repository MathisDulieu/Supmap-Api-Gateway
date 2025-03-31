package com.novus.api_gateway.service;

import com.novus.api_gateway.configuration.EnvConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import static io.jsonwebtoken.lang.Strings.hasText;

@Component
@RequiredArgsConstructor
public class JwtTokenService {

    private final EnvConfiguration envConfiguration;

    private Key getSigningKey() {
        String secretString = envConfiguration.getJwtSecret();
        byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String resolveUserIdFromRequest(HttpServletRequest request) {
        String token = verifyTokenFormat(request);
        return token == null ? null : resolveUserIdFromToken(token);
    }

    public String resolveUserIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    private String verifyTokenFormat(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!hasText(bearerToken) || !bearerToken.startsWith("Bearer ")) {
            return null;
        }

        String token = bearerToken.substring(7);
        return isValidTokenFormat(token) ? token : null;
    }

    private boolean isValidTokenFormat(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

}
