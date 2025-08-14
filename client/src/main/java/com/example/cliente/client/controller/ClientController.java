package com.example.cliente.client.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import com.example.cliente.client.model.Clients;
import com.example.cliente.client.services.ClientService;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import java.util.Map;

@RestController
// permitir el origen del frontend (ajusta si tu frontend está en otro host/puerto)
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // SOLO ADMIN puede listar todos
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Clients> getAllClients() {
        return clientService.getAllClients();
    }

    // ADMIN o USER puede ver por id
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        Optional<Clients> maybe = clientService.getClientById(id);
        return maybe.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    // en ClientController (cliente-service)
@PreAuthorize("hasAnyRole('ADMIN','USER')") // cualquiera autenticado puede usarlo (o restringe si quieres)
@GetMapping("/by-username/{username}")
public ResponseEntity<?> getByUsername(@PathVariable String username) {
    return clientService.findByUsername(username)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}

    // ADMIN crea clientes
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createClient(@Valid @RequestBody Clients client) {
        try {
            Clients created = clientService.createClient(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ADMIN o USER puede actualizar: si quieres que USER solo actualice su propio cliente
    // añade comprobación adicional (comparar username con Authentication)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@Valid @PathVariable Long id, @RequestBody Clients client, Authentication auth) {
        try {
            // Si es USER: asegurarse que actualiza su propio cliente
            if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                // no es admin -> comprobar que el username del cliente coincide con auth.getName()
                Optional<Clients> maybe = clientService.getClientById(id);
                if (maybe.isEmpty()) return ResponseEntity.notFound().build();
                if (!maybe.get().getUsername().equals(auth.getName())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Users can only update their own record"));
                }
            }
            Clients updated = clientService.updateClient(id, client);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // SOLO ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // endpoint debug: ver información sobre el auth (útil para tests/curl)
    // lo ponemos en /api/debug/me para que no choque con /api/clients
    @GetMapping("/debug/me")
    public Map<String, Object> me(Authentication auth) {
        return Map.of(
            "principal", auth.getPrincipal(),
            "name", auth.getName(),
            "authenticated", auth.isAuthenticated(),
            "authorities", auth.getAuthorities()
        );
    }
}
