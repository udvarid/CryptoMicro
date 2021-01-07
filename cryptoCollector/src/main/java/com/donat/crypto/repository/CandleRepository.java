package com.donat.crypto.repository;


import java.time.LocalDateTime;

import com.donat.crypto.domain.Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CandleRepository extends JpaRepository<Candle, Long> {

    @Query(value = "SELECT max(c.time) as time FROM candles as c WHERE c.currency_pair = ?1",
            nativeQuery = true)
    LocalDateTime getLatestCandle(String currencyPair);

}
