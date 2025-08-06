package com.example.orders.orders.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client", url = "http://cliente-ms:8081/api/clients")
public interface UserClient {

    @GetMapping("/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);

}
