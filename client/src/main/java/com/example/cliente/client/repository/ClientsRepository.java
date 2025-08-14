package com.example.cliente.client.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cliente.client.model.Clients;

public interface ClientsRepository extends JpaRepository<Clients, Long> {
    Optional<Clients> findByUsername(String username);
}
