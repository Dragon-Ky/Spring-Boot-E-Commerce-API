package com.example.day3_java.service;

import com.example.day3_java.dto.request.LoginRequest;
import com.example.day3_java.dto.request.RegisterRequest;
import com.example.day3_java.dto.response.AuthResponse;
import com.example.day3_java.entity.AppUser;
import com.example.day3_java.entity.Role;
import com.example.day3_java.exception.BadRequestException;
import com.example.day3_java.repository.UserRepository;
import com.example.day3_java.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtService jwtService){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtService=jwtService;
    }
    public AuthResponse register(RegisterRequest request){
        if (userRepository.existsByUsername(request.getUsername())){
            throw new BadRequestException("tên đăng ký đã tồn tại");
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // encode password trước khi lưu
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(),user.getRole().name());
        return  new AuthResponse(token, user.getUsername(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request){
        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()->new BadRequestException("Tên đăng nhập ko hợp lệ"));
        if (!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new BadRequestException(" sai mật khẩu");
        }
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return  new AuthResponse(token,user.getUsername(), user.getRole().name());
    }
}
