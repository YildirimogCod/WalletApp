package com.yildirimog.walletapi.wallet.service;

import com.yildirimog.walletapi.auth.entity.User;
import com.yildirimog.walletapi.auth.repository.UserRepository;
import com.yildirimog.walletapi.wallet.dto.WalletDTO;
import com.yildirimog.walletapi.wallet.entity.Wallet;
import com.yildirimog.walletapi.wallet.mapper.WalletMapper;
import com.yildirimog.walletapi.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Cüzdan işlemlerini yöneten servis katmanı.
 */
@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final WalletMapper walletMapper;

    /**
     * Giriş yapan kullanıcıya yeni cüzdan oluşturur.
     */
    @Transactional
    public WalletDTO createWallet(WalletDTO walletDTO) {
        User user = getCurrentUser();
        Wallet wallet = walletMapper.toEntity(walletDTO);
        wallet.setUser(user);
        wallet.setBalance(wallet.getBalance() == null ? java.math.BigDecimal.ZERO : wallet.getBalance());
        Wallet saved = walletRepository.save(wallet);
        return walletMapper.toDto(saved);
    }

    /**
     * Giriş yapan kullanıcının tüm cüzdanlarını listeler.
     */
    public List<WalletDTO> getMyWallets() {
        User user = getCurrentUser();
        return walletRepository.findByUser(user)
                .stream()
                .map(walletMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * SecurityContext'ten giriş yapan kullanıcıyı bulur.
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + email));
    }
} 