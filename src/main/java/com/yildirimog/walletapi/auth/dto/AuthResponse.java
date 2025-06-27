package com.yildirimog.walletapi.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Auth işlemlerinde JWT token'ı response olarak döndürür.
 */
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
} 