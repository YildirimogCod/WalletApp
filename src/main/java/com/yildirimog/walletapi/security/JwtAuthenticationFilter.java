package com.yildirimog.walletapi.security;

import com.yildirimog.walletapi.auth.entity.User;
import com.yildirimog.walletapi.auth.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Bu filter, her HTTP isteğinde JWT token'ı kontrol eder.
 * Token geçerliyse kullanıcıyı SecurityContext'e ekler.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. Authorization header'ını al
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. Header yoksa veya Bearer ile başlamıyorsa zinciri devam ettir
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. "Bearer " sonrası token'ı al
        jwt = authHeader.substring(7);
        try {
            // 4. Token'dan kullanıcı adını çek
            username = jwtService.extractUsername(jwt);
        } catch (ExpiredJwtException e) {
            // Token süresi dolmuşsa zinciri devam ettir
            filterChain.doFilter(request, response);
            return;
        } catch (Exception e) {
            // Diğer hatalarda zinciri devam ettir
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Kullanıcı adı varsa ve SecurityContext'te authentication yoksa
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 6. UserDetailsService ile kullanıcıyı yükle
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // 7. Token geçerliyse authentication oluştur
            if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 8. SecurityContext'e authentication'ı ekle
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 9. Zinciri devam ettir
        filterChain.doFilter(request, response);
    }
} 