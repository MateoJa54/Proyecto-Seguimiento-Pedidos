package com.example.cliente.client.security;

import com.example.cliente.client.repository.ClientsRepository;
import com.example.cliente.client.model.Clients;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("clientSecurity")
public class ClientSecurity {

    private final ClientsRepository clientRepository;

    public ClientSecurity(ClientsRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Comprueba si el usuario autenticado es el propietario del cliente con id = clientId
    public boolean isOwner(Long clientId, Authentication authentication) {
        if (authentication == null) return false;

        // Sacamos el username (sub) del token; generalmente principal.getName() contiene el username
        String username = authentication.getName();

        // Busca el cliente en DB
        Clients c = clientRepository.findById(clientId).orElse(null);
        if (c == null) return false;

        // Ajusta la comparación según cómo mapes usuarios <-> clientes.
        // Aquí uso email == username (si tus usuarios se logean con el email).
        return username.equalsIgnoreCase(c.getEmail());
    }
}
