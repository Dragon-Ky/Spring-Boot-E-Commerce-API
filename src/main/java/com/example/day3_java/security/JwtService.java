package com.example.day3_java.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

@Service
public class JwtService {
    private  final Key key;
    private final long expMs;

    public JwtService(JwtProperties props) {
        this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
        this.expMs = props.getExpirationMs();
    }

    public String generateToken(String username,String role){
        return Jwts.builder()
                .setSubject(username)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expMs))
                .signWith(key, SignatureAlgorithm.ES256)
                .compact();
    }
    public Jws<Claims> parseToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
    public String extractUsername(String token){
        return parseToken(token).getBody().getSubject();
    }

    public String extractRole(String token){
        Object role = parseToken(token).getBody().get("role");
        return role == null ? null : role.toString();
    }
}
