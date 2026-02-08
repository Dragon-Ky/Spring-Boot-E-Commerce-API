package com.example.day3_java.controller;

import com.example.day3_java.dto.ApiResponse;
import com.example.day3_java.dto.request.LoginRequest;
import com.example.day3_java.dto.request.RegisterRequest;
import com.example.day3_java.dto.response.AuthResponse;
import com.example.day3_java.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        return ApiResponse.oke(authService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return  ApiResponse.oke(authService.login(request));
    }
}
