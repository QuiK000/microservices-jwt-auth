package com.dev.quikkkk.auth_service.dto.response;

public record AuthenticationResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
