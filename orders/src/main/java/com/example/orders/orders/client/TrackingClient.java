package com.example.orders.orders.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tracking", url = "http://tracking-ms:8084/api/tracking")
public interface TrackingClient {
    @PostMapping("/sync")
    void updateTracking(@RequestBody TrackingDTO tracking);
}
