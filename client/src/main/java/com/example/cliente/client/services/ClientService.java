package com.example.cliente.client.services;


import java.util.List;
import java.util.Optional;

import com.example.cliente.client.model.Clients;

public interface ClientService {
    List <Clients> getAllClients();
    Optional <Clients> getClientById(Long id);
    //Este opcional nos sirve para manejar casos donde el cliente no exista
    //y evitar excepciones innecesarias
    Clients createClient(Clients client);
    Clients updateClient(Long id, Clients client);
    void deleteClient(Long id);

}
