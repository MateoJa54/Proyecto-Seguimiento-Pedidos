package com.example.orders.orders.services;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.orders.orders.model.Order;
import com.example.orders.orders.repository.OrderRepository;

import jakarta.transaction.Transactional;
@Service
@Transactional

public class OrderServiceImplements implements OrderService {

     private final OrderRepository orderRepository;

    public OrderServiceImplements(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return (List<Order>) orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOrder(Order order) {
        // Aquí podrías agregar validaciones adicionales antes de guardar
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        if (orderRepository.existsById(id)) {
            order.setId(id);
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found with id: " + id);
        }
    }

    @Override
    public void deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Order not found with id: " + id);
        }
    }

}
