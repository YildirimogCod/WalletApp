package com.yildirimog.walletapi.auth.security;

import com.yildirimog.walletapi.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * User entity'sini Spring Security'nin UserDetails arayüzüne adapte eder.
 * Böylece User entity'si security zincirinde kullanılabilir.
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    /**
     * Kullanıcının rollerini Spring Security'nin anlayacağı şekilde döndürür.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Hesap süresi doldu mu? (opsiyonel)
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked(); // Hesap kilitli mi?
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Şifre süresi doldu mu? (opsiyonel)
    }

    @Override
    public boolean isEnabled() {
        return user.isActive(); // Hesap aktif mi?
    }

    // User entity'sine erişmek gerekirse ek getter yazabilirsin
    public User getUser() {
        return user;
    }
} 