package com.example.day3_java.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AdminCreateUserRequest {

    @NotBlank(message = "yêu cầu tên đăng nhập")
    private String username;

    @NotBlank(message = "yêu cầu mật khẩu")
    private String password;

    @NotNull(message = "yêu cầu role")
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
