package com.example.day3_java.repository;

import com.example.day3_java.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
