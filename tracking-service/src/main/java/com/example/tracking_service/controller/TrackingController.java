package com.example.tracking_service.controller;

import com.example.tracking_service.dto.TrackingDTO;
import com.example.tracking_service.model.TrackingCache;
import com.example.tracking_service.service.TrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tracking")
@CrossOrigin(origins = "*")
public class TrackingController {

    private final TrackingService service;

    public TrackingController(TrackingService service) {
        this.service = service;
    }

    @PostMapping("/sync")
    public ResponseEntity<?> syncTracking(@RequestBody TrackingDTO dto) {
        if (dto == null || dto.getOrderId() == null) {
            return ResponseEntity.badRequest().body("orderId is required");
        }
        if (dto.getTimestamp() == null) {
            dto.setTimestamp(LocalDateTime.now());
        }
        try {
            service.updateTracking(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(503).body("Redis error: " + e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getTracking(@PathVariable Long orderId) {
        TrackingCache cache = service.getTracking(orderId);
        if (cache == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cache);
    }
}
