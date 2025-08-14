package com.example.orders.orders.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.orders.orders.config.FeignClientConfig;

@FeignClient(
    name = "cliente",
    url = "${CLIENT_SERVICE_URL:http://cliente-ms:8081}/api/clients",
    configuration = FeignClientConfig.class
)
public interface UserClient {

    @GetMapping("/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);

    @GetMapping("/by-username/{username}")
    UserDTO getByUsername(@PathVariable("username") String username);
}
