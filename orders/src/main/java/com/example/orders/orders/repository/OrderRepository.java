package com.example.orders.orders.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.orders.orders.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClienteId(Long clienteId);
}
