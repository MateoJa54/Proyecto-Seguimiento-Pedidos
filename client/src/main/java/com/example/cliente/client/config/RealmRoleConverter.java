package com.example.cliente.client.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.core.convert.converter.Converter;




public class RealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
           List<String> roles = jwt.getClaimAsStringList("roles");

        if (roles == null || roles.isEmpty()) {
            return List.of();
        }

        return roles.stream()
                .map(role -> "ROLE_" + role.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
    }
    

   
    


