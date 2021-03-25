package com.donat.crypto.user.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletHistoryDto {

    private LocalDateTime time;

    private Double amount;

    private Set<WalletDto> detailedAmount;

}
