package com.dev.quikkkk.auth_service.service.impl;

import com.dev.quikkkk.auth_service.dto.request.RegistrationRequest;
import com.dev.quikkkk.auth_service.entity.Role;
import com.dev.quikkkk.auth_service.entity.User;
import com.dev.quikkkk.auth_service.mapper.UserMapper;
import com.dev.quikkkk.auth_service.repository.RoleRepository;
import com.dev.quikkkk.auth_service.repository.UserRepository;
import com.dev.quikkkk.auth_service.service.IAuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final static String TOKEN_TYPE = "Bearer ";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public void register(RegistrationRequest request) {
        boolean emailExists = userRepository.existsByEmailIgnoreCase(request.email());
        if (emailExists) throw new RuntimeException("Пошта вже зареєстрована");
        if (request.password() == null || !request.password().equals(request.confirmPassword()))
            throw new RuntimeException("Невідповідність пароля");

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Ролі не існує"));

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(roles);

        userRepository.save(user);
    }
}
