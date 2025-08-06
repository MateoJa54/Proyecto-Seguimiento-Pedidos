package com.example.cliente.client.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.cliente.client.model.Clients;

public interface ClientsRepository extends CrudRepository<Clients, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar por nombre o email
    

}
