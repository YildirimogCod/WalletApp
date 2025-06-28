package com.yildirimog.walletapi.wallet.repository;

import com.yildirimog.walletapi.wallet.entity.Wallet;
import com.yildirimog.walletapi.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Wallet entity'si için CRUD işlemlerini sağlar.
 */
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    // Belirli bir kullanıcıya ait cüzdanları bulmak için
    List<Wallet> findByUser(User user);
} 