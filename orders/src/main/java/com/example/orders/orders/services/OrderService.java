package com.example.orders.orders.services;
import com.example.orders.orders.model.Order;
import java.util.List;
import java.util.Optional;
public interface OrderService {
   List<Order> getAllOrders();
    
    Optional<Order> getOrderById(Long id);
    
    Order createOrder(Order order);
    
    Order updateOrder(Long id, Order order);
    
    void deleteOrder(Long id);

}
