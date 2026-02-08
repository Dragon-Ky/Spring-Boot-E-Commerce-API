package com.example.day3_java.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {
    @NotBlank(message = "name ko được để trống")
    private String username;

    @NotBlank(message = "yêu cầu nhập mật khẩu")
    private String password;

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
}
