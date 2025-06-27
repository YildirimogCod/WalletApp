package com.yildirimog.walletapi.auth.controller;

import com.yildirimog.walletapi.auth.dto.AuthResponse;
import com.yildirimog.walletapi.auth.dto.LoginRequest;
import com.yildirimog.walletapi.auth.dto.RegisterRequest;
import com.yildirimog.walletapi.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Kullanıcı kayıt ve giriş işlemleri için endpoint'leri sağlar.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Kullanıcı kayıt endpoint'i
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Kullanıcı giriş endpoint'i
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
} 