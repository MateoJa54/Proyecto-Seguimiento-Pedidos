package com.example.cliente.client.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DebugController {

    @GetMapping("/api/debug/me")
    public Map<String, Object> me(Authentication auth) {
        return Map.of(
            "principal", auth.getPrincipal(),
            "name", auth.getName(),
            "authenticated", auth != null && auth.isAuthenticated(),
            "authorities", auth != null ? auth.getAuthorities() : null
        );
    }
}
