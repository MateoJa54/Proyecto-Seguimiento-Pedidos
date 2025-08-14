package com.example.orders.orders.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.orders.orders.client.UserClient;
import com.example.orders.orders.client.UserDTO;
import com.example.orders.orders.client.TrackingClient;
import com.example.orders.orders.client.TrackingDTO;
import com.example.orders.orders.model.Order;
import com.example.orders.orders.services.OrderService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
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

    // ADMIN: todas las órdenes; USER: solo las suyas (buscamos cliente por username)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<?> getAllOrders(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return ResponseEntity.ok(orderService.getAllOrders());
        } else {
            // obtener username del token
            String username = auth.getName();
            try {
                UserDTO userDto = userClient.getByUsername(username);
                if (userDto == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Client not found"));
                List<Order> orders = orderService.getOrdersByClienteId(userDto.getId());
                return ResponseEntity.ok(orders);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Error fetching client: "+e.getMessage()));
            }
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id, Authentication auth) {
        Optional<Order> maybe = orderService.getOrderById(id);
        if (maybe.isEmpty()) return ResponseEntity.notFound().build();
        Order order = maybe.get();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return ResponseEntity.ok(order);

        // USER: sólo si pertenece al cliente del username
        try {
            UserDTO userDto = userClient.getByUsername(auth.getName());
            if (userDto == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","No client for user"));
            if (!order.getClienteId().equals(userDto.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","Forbidden"));
            }
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Error fetching client: "+e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody Order order) {
        // Validar que cliente exista
        try {
            UserDTO user = userClient.getUserById(order.getClienteId());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Cliente no existe"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Error al consultar cliente: "+e.getMessage()));
        }

        Order saved = orderService.createOrder(order);
        TrackingDTO trackingDTO = new TrackingDTO(saved.getId(), saved.getEstado(), LocalDateTime.now());
        try { trackingClient.updateTracking(trackingDTO); } catch (Exception ignored) {}
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        try {
            Order updated = orderService.updateOrder(id, order);
            TrackingDTO trackingDTO = new TrackingDTO(updated.getId(), updated.getEstado(), LocalDateTime.now());
            try { trackingClient.updateTracking(trackingDTO); } catch (Exception ignored) {}
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // debug endpoint
    @GetMapping("/debug/me")
    public Map<String,Object> me(Authentication auth) {
        return Map.of(
            "name", auth.getName(),
            "authorities", auth.getAuthorities()
        );
    }
}
