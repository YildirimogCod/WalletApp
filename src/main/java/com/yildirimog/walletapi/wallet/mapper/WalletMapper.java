package com.yildirimog.walletapi.wallet.mapper;

import com.yildirimog.walletapi.wallet.dto.WalletDTO;
import com.yildirimog.walletapi.wallet.entity.Wallet;
import org.mapstruct.Mapper;

/**
 * Wallet entity'si ile WalletDTO arasında dönüşüm sağlar.
 */
@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletDTO toDto(Wallet wallet);
    Wallet toEntity(WalletDTO dto);
} 