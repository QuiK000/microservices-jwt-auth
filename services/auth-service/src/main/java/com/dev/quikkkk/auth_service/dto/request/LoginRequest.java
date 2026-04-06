package com.dev.quikkkk.auth_service.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
