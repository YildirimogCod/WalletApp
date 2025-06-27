package com.yildirimog.walletapi.auth.dto;

import lombok.Data;

/**
 * Kullanıcı kayıt isteği için gerekli alanları içerir.
 */
@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String name; // İsteğe bağlı, örnek alan
} 