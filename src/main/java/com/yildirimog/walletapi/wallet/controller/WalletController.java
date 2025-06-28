package com.yildirimog.walletapi.wallet.controller;

import com.yildirimog.walletapi.wallet.dto.WalletDTO;
import com.yildirimog.walletapi.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kullanıcıya cüzdan oluşturma ve kendi cüzdanlarını listeleme işlemleri için endpointler.
 * JWT ile korumalıdır.
 */
@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    /**
     * Giriş yapan kullanıcıya yeni cüzdan oluşturur.
     */
    @PostMapping
    public ResponseEntity<WalletDTO> createWallet(@RequestBody WalletDTO walletDTO) {
        WalletDTO created = walletService.createWallet(walletDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * Giriş yapan kullanıcının tüm cüzdanlarını listeler.
     */
    @GetMapping
    public ResponseEntity<List<WalletDTO>> getMyWallets() {
        List<WalletDTO> wallets = walletService.getMyWallets();
        return ResponseEntity.ok(wallets);
    }
} 