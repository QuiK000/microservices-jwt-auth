package com.dev.quikkkk.auth_service.dto.request;

public record RegistrationRequest(
        String email,
        String password,
        String confirmPassword
) {
}
