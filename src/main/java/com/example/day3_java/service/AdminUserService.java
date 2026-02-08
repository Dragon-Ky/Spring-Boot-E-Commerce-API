package com.example.day3_java.service;

import com.example.day3_java.dto.request.AdminCreateUserRequest;
import com.example.day3_java.entity.AppUser;
import com.example.day3_java.entity.Role;
import com.example.day3_java.exception.BadRequestException;
import com.example.day3_java.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }
    public void createUser(AdminCreateUserRequest request){
        if (userRepository.existsByUsername(request.getUsername())){
            throw  new BadRequestException("Username already exists");
        }
        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        }catch (Exception exception){
            throw new BadRequestException("Role phải là USER hoặc ADMIN");
        }
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);
    }
}
