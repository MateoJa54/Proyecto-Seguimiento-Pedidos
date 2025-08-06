package com.example.orders.orders.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.example.orders.orders.model.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
List<Order> findByClienteId(Long clienteId);

}
