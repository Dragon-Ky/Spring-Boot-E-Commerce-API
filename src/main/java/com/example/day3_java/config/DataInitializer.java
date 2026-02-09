package com.example.day3_java.config;

import com.example.day3_java.entity.AppUser;
import com.example.day3_java.entity.Role;
import com.example.day3_java.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin(){
        if (!userRepository.existsByRole(Role.ADMIN)){
            AppUser admin = new AppUser();
            admin.setUsername("Admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
        }
    }
}
