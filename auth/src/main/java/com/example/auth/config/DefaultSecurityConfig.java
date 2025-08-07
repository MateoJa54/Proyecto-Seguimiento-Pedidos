package com.example.auth.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DefaultSecurityConfig {

    /** 1) PasswordEncoder */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** 2) Crea usuarios/roles en la base de datos */
    @Bean
    public UserDetailsManager users(DataSource dataSource, PasswordEncoder encoder) {
        JdbcUserDetailsManager mgr = new JdbcUserDetailsManager(dataSource);
        if (!mgr.userExists("admin")) {
            mgr.createUser(
                org.springframework.security.core.userdetails.User
                    .withUsername("admin")
                    .password(encoder.encode("adminpass"))
                    .roles("ADMIN")
                    .build()
            );
        }
        if (!mgr.userExists("cliente")) {
            mgr.createUser(
                org.springframework.security.core.userdetails.User
                    .withUsername("cliente")
                    .password(encoder.encode("cliente123"))
                    .roles("CLIENTE")
                    .build()
            );
        }
        return mgr;
    }

    /** 3) Seguridad para /login y acceso público a endpoints del Auth Server */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/error", "/oauth2/**", "/.well-known/**").permitAll()
            .anyRequest().authenticated()
          )
          .formLogin(Customizer.withDefaults());
        return http.build();
    }
}

