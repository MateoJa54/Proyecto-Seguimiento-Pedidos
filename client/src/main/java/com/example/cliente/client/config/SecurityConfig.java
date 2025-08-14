package com.example.cliente.client.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Habilita CORS usando el CorsConfigurationSource bean
        http.cors(Customizer.withDefaults());

        // Deshabilita CSRF para API REST (si usas cookies, revisa)
        http.csrf(csrf -> csrf.disable());

        // Configuración de autorización:
        http.authorizeHttpRequests(auth -> auth
            // Rutas de la API
            .requestMatchers(HttpMethod.GET, "/api/clients/**").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.POST, "/api/clients/**").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.PUT, "/api/clients/**").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.DELETE, "/api/clients/**").hasRole("ADMIN")
            // endpoints públicos si los hubiera
            .requestMatchers("/public/**").permitAll()
            // resto requiere autenticación
            .anyRequest().authenticated()
        );

        // Configurar resource server JWT (decodificador automático si pones issuer-uri en properties)
        http.oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new RealmRoleConverter());
        return converter;
    }

    /**
     * CORS configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Orígenes permitidos (desarrollo)
        config.setAllowedOrigins(List.of(
            "http://localhost:4200",
            "http://127.0.0.1:4200"
        ));

        // Métodos permitidos
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));

        // Headers permitidos; importante permitir Authorization
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        // Si quieres permitir cualquier header (más permisivo):
        // config.setAllowedHeaders(List.of("*"));

        // Permitir credenciales (cookies) si se usan (para tokens no es necesario)
        config.setAllowCredentials(true);

        // Duración del preflight en segundos
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // aplica a todas las rutas
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
