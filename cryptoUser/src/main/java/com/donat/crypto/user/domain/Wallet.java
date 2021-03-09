package com.donat.crypto.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

import com.donat.crypto.user.domain.enums.CCY;
import com.donat.crypto.user.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_generator")
    @SequenceGenerator(name = "wallet_generator", sequenceName = "wallet_seq")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CCY ccy;

    @Column(name = "amount")
    private Double amount;

    @UpdateTimestamp
    private LocalDateTime timeOfTransaction;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Version
    @Column(name = "version")
    private long version;
}
