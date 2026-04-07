package com.dev.quikkkk.auth_service.service.impl;

import com.dev.quikkkk.auth_service.entity.User;
import com.dev.quikkkk.auth_service.repository.UserRepository;
import com.dev.quikkkk.auth_service.security.SecurityUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmailWithRoles(email)
                .orElseThrow(() -> new EntityNotFoundException("Користувача не знайдено"));
        return new SecurityUser(user);
    }
}
