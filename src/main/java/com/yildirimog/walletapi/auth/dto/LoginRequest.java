package com.yildirimog.walletapi.auth.dto;

import lombok.Data;

/**
 * Kullanıcı login isteği için gerekli alanları içerir.
 */
@Data
public class LoginRequest {
    private String email;
    private String password;
} 