package com.dev.quikkkk.auth_service.service.impl;

import com.dev.quikkkk.auth_service.entity.User;
import com.dev.quikkkk.auth_service.service.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements IJwtService {
    @Override
    public String generateAccessToken(User user) {
        return "";
    }

    @Override
    public String generateRefreshToken(User user) {
        return "";
    }

    @Override
    public String extractEmail(String token) {
        return "";
    }

    @Override
    public String extractUserId(String token) {
        return "";
    }

    @Override
    public String extractTokenType(String token) {
        return "";
    }

    @Override
    public List<String> extractRoles(String token) {
        return List.of();
    }

    @Override
    public Date extractExpiration(String token) {
        return null;
    }

    @Override
    public boolean isTokenExpired(String token) {
        return false;
    }

    @Override
    public boolean isRefreshToken(String token) {
        return false;
    }
}
