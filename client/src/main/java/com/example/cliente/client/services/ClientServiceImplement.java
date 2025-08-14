package com.example.cliente.client.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cliente.client.model.Clients;
import com.example.cliente.client.repository.ClientsRepository;

@Service
@Transactional
public class ClientServiceImplement implements ClientService {

    private final ClientsRepository clientsRepository;

    public ClientServiceImplement(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    @Override
    public List<Clients> getAllClients() {
        return clientsRepository.findAll();
    }

    @Override
    public Optional<Clients> getClientById(Long id) {
        return clientsRepository.findById(id);
    }

    @Override
    public Clients createClient(Clients client) {
        if (client.getId() != null) {
            throw new IllegalArgumentException("New clients must not have an ID");
        }
        // Evitar duplicados por username/email:
        clientsRepository.findByUsername(client.getUsername()).ifPresent(c -> {
            throw new IllegalArgumentException("Username already exists");
        });
        // El email lo marcaste unique en la entidad; igualmente podrías comprobarlo aquí.
        return clientsRepository.save(client);
    }

    @Override
    public Clients updateClient(Long id, Clients client) {
        if (!clientsRepository.existsById(id)) {
            throw new IllegalArgumentException("Client with ID " + id + " does not exist");
        }
        client.setId(id);
        return clientsRepository.save(client);
    }

    @Override
    public void deleteClient(Long id) {
        if (!clientsRepository.existsById(id)) {
            throw new IllegalArgumentException("Client with ID " + id + " does not exist");
        }
        clientsRepository.deleteById(id);
    }

    @Override
    public Optional<Clients> findByUsername(String username) {
        return clientsRepository.findByUsername(username);
    }
}
