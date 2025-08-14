package com.example.oauth2.oauth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.oauth2.oauth2.model.AppUser;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
