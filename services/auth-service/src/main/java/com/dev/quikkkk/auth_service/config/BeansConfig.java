package com.dev.quikkkk.auth_service.config;

import com.dev.quikkkk.auth_service.entity.Role;
import com.dev.quikkkk.auth_service.repository.RoleRepository;
import com.dev.quikkkk.auth_service.security.ApplicationAuditorAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@Slf4j
public class BeansConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner commandLineRunner(RoleRepository repository) {
        return _ -> {
            createRoleIfNotExists(repository, "ADMIN");
            createRoleIfNotExists(repository, "USER");
            createRoleIfNotExists(repository, "MODERATOR");
        };
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new ApplicationAuditorAware();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    private void createRoleIfNotExists(RoleRepository repository, String roleName) {
        Optional<Role> existingRole = repository.findByName(roleName);
        log.info("Перевірка чи існує {} роль", roleName);

        if (existingRole.isEmpty()) {
            Role role = Role.builder().name(roleName).build();

            repository.save(role);
            log.info("Роль {} створена", roleName);
        }
    }
}
