package com.example.oauth2.oauth2.service;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import com.example.oauth2.oauth2.repository.UserRepository;
import com.example.oauth2.oauth2.model.AppUser;

@Service
public class AppUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        List<SimpleGrantedAuthority> auths = u.getRolesList().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();
        return User.withUsername(u.getUsername())
                .password(u.getPassword())
                .authorities(auths)
                .build();
    }

    public AppUser save(AppUser u) { return userRepository.save(u); }

    public boolean existsByUsername(String username) { return userRepository.existsByUsername(username); }

    public boolean existsByEmail(String email) { return userRepository.existsByEmail(email); }
}
