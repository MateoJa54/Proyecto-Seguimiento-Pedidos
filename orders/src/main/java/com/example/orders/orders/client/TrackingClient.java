package com.example.orders.orders.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.orders.orders.config.FeignClientConfig;

@FeignClient(name = "tracking", url = "${TRACKING_SERVICE_URL:http://tracking-ms:8084}/api/tracking", configuration = FeignClientConfig.class)
public interface TrackingClient {
    @PostMapping("/sync")
    void updateTracking(@RequestBody TrackingDTO tracking);
}
