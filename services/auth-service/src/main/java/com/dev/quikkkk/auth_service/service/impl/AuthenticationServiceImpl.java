package com.dev.quikkkk.auth_service.service.impl;

import com.dev.quikkkk.auth_service.dto.request.LoginRequest;
import com.dev.quikkkk.auth_service.dto.request.RegistrationRequest;
import com.dev.quikkkk.auth_service.dto.response.AuthenticationResponse;
import com.dev.quikkkk.auth_service.entity.Role;
import com.dev.quikkkk.auth_service.entity.User;
import com.dev.quikkkk.auth_service.mapper.UserMapper;
import com.dev.quikkkk.auth_service.repository.RoleRepository;
import com.dev.quikkkk.auth_service.repository.UserRepository;
import com.dev.quikkkk.auth_service.service.IAuthenticationService;
import com.dev.quikkkk.auth_service.service.IJwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;

    @Override
    public void register(RegistrationRequest request) {
        boolean emailExists = userRepository.existsByEmailIgnoreCase(request.email());
        if (emailExists) throw new RuntimeException("Пошта вже зареєстрована");
        if (request.password() == null || !request.password().equals(request.confirmPassword()))
            throw new RuntimeException("Невідповідність пароля");

        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new EntityNotFoundException("Ролі не існує"));

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("...")); // TODO

        if (!user.isEnabled()) throw new RuntimeException("..."); // TODO

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        CompletableFuture<String> accessTokenFuture = CompletableFuture.supplyAsync(
                () -> jwtService.generateAccessToken(user)
        );

        CompletableFuture<String> refreshTokenFuture = CompletableFuture.supplyAsync(
                () -> jwtService.generateRefreshToken(user)
        );

        String accessToken = accessTokenFuture.join();
        String refreshToken = refreshTokenFuture.join();

        return userMapper.toResponse(accessToken, refreshToken);
    }
}
