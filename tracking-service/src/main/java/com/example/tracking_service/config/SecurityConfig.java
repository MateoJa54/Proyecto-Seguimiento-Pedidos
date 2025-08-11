package com.example.tracking_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> auth
              // Protege el endpoint de escritura (sync). Solo tokens válidos pueden actualizar.
              .requestMatchers("/api/tracking/sync").authenticated()
              // Opcional: permitir actuator health sin token
              .requestMatchers("/actuator/health").permitAll()
              // Dejar lectura pública para baja latencia:
              .requestMatchers("/api/tracking/**").permitAll()
              .anyRequest().permitAll()
          )
          .oauth2ResourceServer(oauth2 -> oauth2
              .jwt()
          );

        return http.build();
    }
}
