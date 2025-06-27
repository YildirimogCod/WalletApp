package com.yildirimog.walletapi.auth.service;

import com.yildirimog.walletapi.auth.dto.AuthResponse;
import com.yildirimog.walletapi.auth.dto.LoginRequest;
import com.yildirimog.walletapi.auth.dto.RegisterRequest;
import com.yildirimog.walletapi.auth.entity.Role;
import com.yildirimog.walletapi.auth.entity.User;
import com.yildirimog.walletapi.auth.repository.RoleRepository;
import com.yildirimog.walletapi.auth.repository.UserRepository;
import com.yildirimog.walletapi.security.JwtService;
import com.yildirimog.walletapi.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Kullanıcı kayıt, giriş ve JWT üretimi işlemlerini yönetir.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    /**
     * Kullanıcı kaydı (register) işlemi.
     * Şifre hash'lenir, rol atanır, kullanıcı kaydedilir ve JWT üretilir.
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Bu email ile kayıtlı kullanıcı zaten var.");
        }
        // Varsayılan rolü bul veya oluştur
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "USER")));
        // MapStruct ile DTO'dan User entity'si oluştur
        User user = userMapper.toEntity(request);
        // Manuel olarak kalan alanları set et
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton(userRole));
        user.setActive(true);
        user.setLocked(false);
        userRepository.save(user);
        // JWT üret
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    /**
     * Kullanıcı giriş (login) işlemi.
     * Kimlik doğrulama başarılıysa JWT üretilir.
     */
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Email veya şifre hatalı.");
        }
        // JWT üret
        String token = jwtService.generateToken(request.getEmail());
        return new AuthResponse(token);
    }
} 