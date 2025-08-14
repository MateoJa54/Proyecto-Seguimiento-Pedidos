package com.example.oauth2.oauth2.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Controller;

import com.example.oauth2.oauth2.service.AppUserService;
import com.example.oauth2.oauth2.model.AppUser;
import com.example.oauth2.oauth2.dto.RegistrationRequest;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final String clientServiceUrl; // ex: http://cliente-ms:8081

    public RegistrationController(AppUserService appUserService,
                                  PasswordEncoder passwordEncoder,
                                  RestTemplate restTemplate,
                                  @Value("${CLIENT_SERVICE_URL:http://cliente-ms:8081}") String clientServiceUrl) {
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
        this.clientServiceUrl = clientServiceUrl;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest req) {
        if (req == null || req.username == null || req.password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "username and password required"));
        }
        if (appUserService.existsByUsername(req.username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","username already taken"));
        }
        if (req.email != null && appUserService.existsByEmail(req.email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","email already used"));
        }

        AppUser user = new AppUser();
        user.setUsername(req.username);
        user.setPassword(passwordEncoder.encode(req.password));
        user.setName(req.name);
        user.setEmail(req.email);
        user.setPhone(req.phone);
        user.setAddress(req.address);
        user.setRolesList(java.util.List.of("USER"));

        AppUser savedUser = appUserService.save(user);

        // Crear cliente en client-service (endpoint público /api/clients/register)
        try {
            Map<String, Object> clientPayload = Map.of(
                "name", req.name != null ? req.name : req.username,
                "email", req.email,
                "phone", req.phone,
                "address", req.address,
                "username", req.username
            );
            // POST /api/clients/register
            String url = clientServiceUrl + "/api/clients/register";
            restTemplate.postForEntity(url, clientPayload, Map.class);
        } catch (Exception ex) {
            // Si la creación del cliente falla, podemos:
            // - eliminar el usuario creado (rollback manual) o
            // - devolver error indicando crear el cliente manualmente.
            // Aquí intentamos eliminar y devolvemos error:
            // (mejor sería manejar transacciones distribuidas; aquí simplificamos)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body(Map.of("error", "user created but failed to create client: " + ex.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("username", savedUser.getUsername()));
    }
}
