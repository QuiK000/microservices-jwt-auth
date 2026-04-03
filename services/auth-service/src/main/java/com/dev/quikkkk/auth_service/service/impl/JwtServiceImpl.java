package com.dev.quikkkk.auth_service.service.impl;

import com.dev.quikkkk.auth_service.entity.Role;
import com.dev.quikkkk.auth_service.entity.User;
import com.dev.quikkkk.auth_service.service.IJwtService;
import com.dev.quikkkk.auth_service.utils.KeyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtServiceImpl implements IJwtService {
    private static final String TOKEN_TYPE = "token_type";
    private static final String USER_ID = "userId";
    private static final String PATH_TO_PRIVATE_KEY = "keys/local-only/private_key.pem";
    private static final String PATH_TO_PUBLIC_KEY = "keys/local-only/public_key.pem";
    private static final PrivateKey PRIVATE_KEY;
    private static final PublicKey PUBLIC_KEY;

    static {
        try {
            PRIVATE_KEY = KeyUtils.loadPrivateKey(PATH_TO_PRIVATE_KEY);
            PUBLIC_KEY = KeyUtils.loadPublicKey(PATH_TO_PUBLIC_KEY);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JWT Keys", e);
        }
    }

    @Value("${app.security.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("{app.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Override
    public String generateAccessToken(User user) {
        Map<String, Object> claims = Map.of(
                TOKEN_TYPE, "ACCESS_TOKEN",
                USER_ID, user.getId(),
                "roles", user.getRoles().stream().map(Role::getName).toList()
        );

        return buildToken(user.getEmail(), claims, accessTokenExpiration);
    }

    @Override
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = Map.of(
                TOKEN_TYPE, "REFRESH_TOKEN",
                USER_ID, user.getId(),
                "roles", user.getRoles().stream().map(Role::getName).toList()
        );

        return buildToken(user.getEmail(), claims, refreshTokenExpiration);
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        Claims claims = extractClaims(refreshToken);

        if (!"REFRESH_TOKEN".equals(claims.get(TOKEN_TYPE))) throw new RuntimeException("Invalid token type");
        if (isTokenExpired(refreshToken)) throw new RuntimeException("Token expired");

        String email = claims.getSubject();
        String userId = claims.get(USER_ID).toString();

        Map<String, Object> claimsForNewToken = Map.of(
                TOKEN_TYPE, "ACCESS_TOKEN",
                USER_ID, userId,
                "roles", claims.get("roles")
        );

        return buildToken(email, claimsForNewToken, accessTokenExpiration);
    }

    @Override
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public String extractUserId(String token) {
        return extractClaims(token).getId();
    }

    @Override
    public String extractTokenType(String token) {
        return extractClaims(token).get(TOKEN_TYPE).toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return (List<String>) extractClaims(token).get("roles");
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    @Override
    public boolean isTokenValid(String token, String expectedEmail) {
        return false;
    }

    private String buildToken(String email, Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(PRIVATE_KEY)
                .compact();
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(PUBLIC_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }
}
