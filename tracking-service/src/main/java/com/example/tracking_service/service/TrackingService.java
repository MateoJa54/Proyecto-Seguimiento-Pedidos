package com.example.tracking_service.service;
import com.example.tracking_service.dto.TrackingDTO;
import com.example.tracking_service.model.TrackingCache;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import java.time.LocalDateTime;
import java.time.Duration;

@Service
public class TrackingService {

    private final RedisTemplate<String, TrackingCache> redisTemplate;
    private final Duration ttl = Duration.ofDays(30);

    public TrackingService(RedisTemplate<String, TrackingCache> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String key(Long orderId) {
        return "tracking:" + orderId;
    }

    public void updateTracking(TrackingDTO dto) {
        LocalDateTime ts = dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now();
        TrackingCache cache = new TrackingCache(dto.getOrderId(), dto.getStatus(), ts);
        redisTemplate.opsForValue().set(key(dto.getOrderId()), cache, ttl);
    }

    public TrackingCache getTracking(Long orderId) {
        return redisTemplate.opsForValue().get(key(orderId));
    }
}
