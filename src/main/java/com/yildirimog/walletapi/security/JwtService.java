package com.yildirimog.walletapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Bu servis JWT token üretimi, doğrulaması ve token'dan bilgi çekme işlemlerini yapar.
 * JWT (JSON Web Token), kullanıcı kimliğini güvenli şekilde taşımak için kullanılır.
 */
@Service
public class JwtService {
    // Token'ı imzalamak için kullanılan gizli anahtar.
    @Value("${jwt.secret}")
    private String secretKey;

    // Token'ın geçerli olacağı süre (ör: 24 saat)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    /**
     * Kullanıcı adı (veya email) ile yeni bir JWT token üretir.
     * @param username Kullanıcı adı veya email
     * @return Üretilen JWT token
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Token'ı oluşturur ve imzalar.
     * @param claims Token'a eklenmek istenen ekstra bilgiler (isteğe bağlı)
     * @param subject Token'ın sahibi (genellikle username/email)
     * @return İmzalanmış JWT token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims) // Ekstra bilxiler
                .subject(subject) // Token'ın sahibi
                .issuedAt(new Date(System.currentTimeMillis())) // Oluşturulma zamanı
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Son kullanma zamanı
                .signWith(getSigningKey()) // İmzalama
                .compact();
    }
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Token'dan kullanıcı adını (subject) çeker.
     * @param token JWT token
     * @return Kullanıcı adı/email
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Token'dan istenen claim'i çeker (ör: subject, expiration vs.)
     * @param token JWT token
     * @param claimsResolver Claim'i işleyen fonksiyon
     * @return İstenen bilgi
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Token'ın geçerli olup olmadığını kontrol eder.
     * @param token JWT token
     * @param username Beklenen kullanıcı adı/email
     * @return Token geçerli mi?
     */
    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * Token'ın süresi dolmuş mu kontrol eder.
     * @param token JWT token
     * @return Token süresi dolmuş mu?
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Token'dan son kullanma tarihini çeker.
     * @param token JWT token
     * @return Son kullanma tarihi
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Token'daki tüm claim'leri çözümler.
     * @param token JWT token
     * @return Tüm claim'ler
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
} 