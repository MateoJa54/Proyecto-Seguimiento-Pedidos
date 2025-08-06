package com.example.orders.orders.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.orders.orders.model.Order;

import com.example.orders.orders.services.OrderService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:8080") // Cambia al puerto de tu frontend Angular si es otro
@RequestMapping("/api/orders")
public class OrdersController {
    
    private final OrderService orderService;
    private final UserClient userClient;

    public OrdersController(OrderService orderService, UserClient userClient) {
        this.orderService = orderService;
        this.userClient = userClient;
    }

    

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
public ResponseEntity<?> createOrder(@Valid @RequestBody Order order) {
    // Verificar si el cliente existe mediante el UserClient
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

    // Si existe, se crea la orden
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderService.createOrder(order));
}

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable Long id, @RequestBody Order order) {
        try {
            return ResponseEntity.ok(orderService.updateOrder(id, order));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

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
