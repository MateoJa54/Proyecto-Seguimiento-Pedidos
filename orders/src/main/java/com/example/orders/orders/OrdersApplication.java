package com.example.orders.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrdersApplication {

	public static void main(String[] args) {
		System.out.println("🚀 Aplicación Products iniciando...");
		SpringApplication.run(OrdersApplication.class, args);
		System.out.println("Aplicación Products ejecutándose correctamente.");
	}

}
