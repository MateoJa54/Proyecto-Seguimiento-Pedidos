package com.example.orders.orders.client;

import java.time.LocalDateTime;

public class TrackingDTO {
      private Long orderId;
    private String status;
    private LocalDateTime timestamp;

    // Constructors
    public TrackingDTO() {}

    public TrackingDTO(Long orderId, String status, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
   
}
