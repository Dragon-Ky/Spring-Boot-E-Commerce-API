package com.example.day3_java.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    // password đã hash (BCrypt), tuyệt đối không lưu plain text

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    // mặc định USER
}
