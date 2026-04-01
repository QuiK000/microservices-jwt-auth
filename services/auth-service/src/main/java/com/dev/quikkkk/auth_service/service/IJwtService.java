package com.dev.quikkkk.auth_service.service;

import com.dev.quikkkk.auth_service.entity.User;

import java.util.Date;
import java.util.List;

public interface IJwtService {
    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String extractEmail(String token);

    String extractUserId(String token);

    String extractTokenType(String token);

    List<String> extractRoles(String token);

    Date extractExpiration(String token);

    boolean isTokenExpired(String token);

    boolean isRefreshToken(String token);
}
