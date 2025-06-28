package com.yildirimog.walletapi.wallet.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * Wallet entity'sinin dış dünyaya taşınacak hali.
 */
@Data
public class WalletDTO {
    private Long id;
    private BigDecimal balance;
    private String currency;
    private String name;
} 