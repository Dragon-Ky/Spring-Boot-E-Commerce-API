package com.example.day3_java.security;

import com.example.day3_java.entity.AppUser;
import com.example.day3_java.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtService jwtService,UserRepository userRepository){
        this.jwtService=jwtService;
        this.userRepository=userRepository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{
        String authHeader = request.getHeader("Authorization");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getServletPath();
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Header chuẩn: Authorization: Bearer <token>
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String token = authHeader.substring(7);
        try {
            String username = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            // nếu chưa có authentication trong context thì set
            if (username != null && SecurityContextHolder.getContext().getAuthentication()==null){
                AppUser user  = userRepository.findByUsername(username).orElse(null);
                if (user != null){
                    // Spring Security role format: "ROLE_ADMIN", "ROLE_USER"
                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_"+role));

                    var auth = new UsernamePasswordAuthenticationToken(
                            user,null,authorities
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (JwtException e){
            // token sai/expired -> không set auth
            // để request rơi vào 401 ở phía Security
        }
        filterChain.doFilter(request,response);
    }
}
