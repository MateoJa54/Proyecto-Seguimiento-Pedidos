package com.example.cliente.client.controller;

import java.util.List;

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

@RestController
@CrossOrigin(origins = "http://localhost:8082") // Cambia esto al origen del frontend
// Permite solicitudes desde el frontend en el puerto 8082
@RequestMapping("/api/clients")
public class ClientController {
private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Clients> getAllClients() {
        return clientService.getAllClients();
    }
@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
@PreAuthorize("hasRole('ADMIN')")

    @PostMapping
    public ResponseEntity<?> createClient(@Valid @RequestBody Clients client) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.createClient(client));
    }
@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@Valid @PathVariable Long id, @RequestBody Clients client) {
        try {
            return ResponseEntity.ok(clientService.updateClient(id, client));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
@PreAuthorize("hasRole('ADMIN')")

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
