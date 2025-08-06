package com.example.orders.orders.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orders.orders.client.UserClient;
import com.example.orders.orders.client.UserDTO;
import com.example.orders.orders.client.TrackingClient;
import com.example.orders.orders.client.TrackingDTO;
import com.example.orders.orders.model.Order;
import com.example.orders.orders.services.OrderService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrderService orderService;
    private final UserClient userClient;
    private final TrackingClient trackingClient;

    public OrdersController(OrderService orderService, UserClient userClient, TrackingClient trackingClient) {
        this.orderService = orderService;
        this.userClient = userClient;
        this.trackingClient = trackingClient;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody Order order) {
        try {
            UserDTO user = userClient.getUserById(order.getClienteId());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El cliente con ID " + order.getClienteId() + " no existe.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al consultar el cliente con ID " + order.getClienteId() + ": " + e.getMessage());
        }

        Order createdOrder = orderService.createOrder(order);

        // Enviar actualización a tracking-service
        TrackingDTO trackingDTO = new TrackingDTO(
                createdOrder.getId(),
                createdOrder.getEstado(),
                LocalDateTime.now()
        );
        try {
            trackingClient.updateTracking(trackingDTO);
        } catch (Exception e) {
            // Puedes implementar un sistema de reintento o cola local aquí
            System.err.println("Error al actualizar tracking-service: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable Long id, @RequestBody Order order) {
        try {
            Order updated = orderService.updateOrder(id, order);

            // Enviar actualización a tracking-service
            TrackingDTO trackingDTO = new TrackingDTO(
                    updated.getId(),
                    updated.getEstado(),
                    LocalDateTime.now()
            );
            try {
                trackingClient.updateTracking(trackingDTO);
            } catch (Exception e) {
                System.err.println("Error al actualizar tracking-service: " + e.getMessage());
            }

            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
