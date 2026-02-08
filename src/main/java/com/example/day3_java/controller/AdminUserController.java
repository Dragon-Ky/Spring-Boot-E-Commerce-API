package com.example.day3_java.controller;

import com.example.day3_java.dto.ApiResponse;
import com.example.day3_java.dto.request.AdminCreateUserRequest;
import com.example.day3_java.service.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService){
        this.adminUserService=adminUserService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<Object> create(@Valid @RequestBody AdminCreateUserRequest request){
        adminUserService.createUser(request);
        return ApiResponse.oke(null);
    }
}
