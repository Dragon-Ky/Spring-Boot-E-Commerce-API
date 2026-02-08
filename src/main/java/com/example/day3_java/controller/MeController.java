package com.example.day3_java.controller;

import com.example.day3_java.dto.ApiResponse;
import com.example.day3_java.dto.response.MeResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MeController {
    @GetMapping("/me")
    public ApiResponse<MeResponse> me(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        String role = auth.getAuthorities().stream()
                .findFirst()
                .map(a->a.getAuthority())
                .orElse("ROLE_USER");
        role = role.replace("ROLE_","");
        return ApiResponse.oke(new MeResponse(username,role));
    }
}
