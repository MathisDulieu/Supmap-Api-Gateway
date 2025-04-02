package com.novus.api_gateway.service;

import com.novus.api_gateway.configuration.EnvConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static io.jsonwebtoken.lang.Strings.hasText;
import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class JwtTokenService {

    private final EnvConfiguration envConfiguration;
    private static final long TOKEN_EXPIRATION_TIME = 172_800_000;

    private Key getSigningKey() {
        String secretString = envConfiguration.getJwtSecret();
        byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(userId)
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isEmailTokenValid(String token) {
        Claims claims = parseTokenClaims(token);
        if (isNull(claims)) {
            return false;
        }

        if (!isEmailConfirmationToken(claims)) {
            return false;
        }

        return isTokenNotExpired(claims);
    }

    public boolean isPasswordResetTokenValid(String token) {
        return isTokenValid(token, "password_reset");
    }

    private boolean isTokenValid(String token, String tokenType) {
        Claims claims = parseTokenClaims(token);
        if (isNull(claims)) {
            return false;
        }

        if (!tokenType.equals(claims.get("type"))) {
            return false;
        }

        return isTokenNotExpired(claims);
    }

    private Claims parseTokenClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isEmailConfirmationToken(Claims claims) {
        return "email_confirmation".equals(claims.get("type"));
    }

    private boolean isTokenNotExpired(Claims claims) {
        Date expirationDate = claims.getExpiration();
        return expirationDate != null && expirationDate.after(new Date());
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
