package com.dev.quikkkk.auth_service.service;

import com.dev.quikkkk.auth_service.dto.request.RegistrationRequest;

public interface IAuthenticationService {
    void register(RegistrationRequest request);
}
