package com.example.cliente.client.services;

import java.util.List;
import java.util.Optional;

import com.example.cliente.client.model.Clients;

public interface ClientService {
    List<Clients> getAllClients();
    Optional<Clients> getClientById(Long id);
    Clients createClient(Clients client);
    Clients updateClient(Long id, Clients client);
    void deleteClient(Long id);

    // útil para encontrar el cliente por username del token
    Optional<Clients> findByUsername(String username);
}
