package com.donat.crypto.user.repository;

import javax.transaction.Transactional;

import com.donat.crypto.user.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface WalletRepository extends JpaRepository<Wallet, Long>  {
}
