package com.example.cliente.client.services;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.cliente.client.model.Clients;
import com.example.cliente.client.repository.ClientsRepository;

@Service
@Transactional
public class ClientServiceImplement implements ClientService{

    private final ClientsRepository clientsRepository;

    public ClientServiceImplement(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    @Override
    public List<Clients> getAllClients() {
        return (List<Clients>)clientsRepository.findAll(); 
    }

    @Override
    public Optional<Clients> getClientById(Long id) {
        return clientsRepository.findById(id);
        // Este método devuelve un Optional, que es una forma segura de manejar
        // la posibilidad de que el cliente no exista.
    }

    @Override
    public Clients createClient(Clients client) {
       if (client.getId() != null) {
            throw new IllegalArgumentException("New clients must not have an ID");
        }
        return clientsRepository.save(client);
        // Este método guarda un nuevo cliente en la base de datos.
        // Si el cliente ya tiene un ID, lanzamos una excepción.
    }

    @Override
    public Clients updateClient(Long id, Clients client) {
        if(clientsRepository.existsById(id)){
            client.setId(id);
            return clientsRepository.save(client);
        } else {
            throw new IllegalArgumentException("Client with ID " + id + " does not exist");
        }
    }

    @Override
    public void deleteClient(Long id) {
        if(clientsRepository.existsById(id)) {
            clientsRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Client with ID " + id + " does not exist");
        }
    }

}
