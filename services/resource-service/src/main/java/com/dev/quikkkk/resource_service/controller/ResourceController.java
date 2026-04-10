package com.dev.quikkkk.resource_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/resources")
public class ResourceController {
    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> getPublicResource() {
        return ResponseEntity.ok(Map.of(
                "message", "Це публічний ресурс.",
                "status", "Доступно всім без токена"
        ));
    }

    @GetMapping("/private")
    public ResponseEntity<Map<String, String>> getPrivateResource() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(Map.of(
                "message", "Це приватний ресурс.",
                "user", Objects.requireNonNull(auth).getName(),
                "roles", auth.getAuthorities().toString()
        ));
    }

    @GetMapping("/admin")
    public ResponseEntity<Map<String, String>> getAdminResource() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(Map.of(
                "message", "Це ресурс виключно для адміністраторів.",
                "user", Objects.requireNonNull(auth).getName(),
                "status", "Доступ дозволено (Role: ADMIN)"
        ));
    }
}
