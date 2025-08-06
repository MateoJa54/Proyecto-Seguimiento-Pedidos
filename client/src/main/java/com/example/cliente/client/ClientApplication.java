package com.example.cliente.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		System.out.println("🚀 Aplicación Products iniciando...");
		SpringApplication.run(ClientApplication.class, args);
		System.out.println("Aplicación Products ejecutándose correctamente.");
	}

}
