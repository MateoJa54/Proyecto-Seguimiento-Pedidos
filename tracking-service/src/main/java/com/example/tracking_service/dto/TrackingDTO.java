package com.example.tracking_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class TrackingDTO {
    private Long orderId;
    private String status;

    // timestamp enviado por pedido-service (opcional)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public TrackingDTO() {}

    public TrackingDTO(Long orderId, String status, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.status = status;
        this.timestamp = timestamp;
    }

    // getters y setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
